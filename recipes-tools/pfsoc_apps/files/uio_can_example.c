/*
 *	Simple UIO CAN Example -- Send and Receive one message in loopback mode
 *
 *	Copyright (c) 2020 Microchip Inc.
 *
 *	Can be freely distributed and used under the terms of the GNU GPL.
 */

#include <sys/types.h>
#include <sys/stat.h>
#include <sys/mman.h>

#include <fcntl.h>
#include <errno.h>
#include <poll.h>
#include <string.h>
#include <stdint.h>
#include <stdlib.h>
#include <unistd.h>
#include <stdio.h>
#include <linux/can.h>

#define NUM_MSGS	(1)

#define NUM_RX_MAILBOXES (32)
#define NUM_TX_MAILBOXES (32)

#define CAN_SET_TSEG2(_tseg2)		(_tseg2 << 5)
#define CAN_SET_TSEG1(_tseg1)		(_tseg1 << 8)
#define CAN_SET_BITRATE(_bitrate)	(_bitrate << 16)

#define CAN_SPEED_8M_1M		(CAN_SET_BITRATE(0) | CAN_SET_TSEG1(4) | CAN_SET_TSEG2(1))

#define CAN_TX_REQ		(1 << 0)
#define CAN_TX_ABORT		(1 << 1)
#define CAN_TX_INT_ENB		(1 << 2)
#define CAN_TX_WPNL_ENB		(1 << 3)
#define CAN_TX_WPNH_ENB		(1 << 23)

#define CAN_RX_MSGAV		(1 << 0)
#define CAN_RX_RTRP		(1 << 1)
#define CAN_RX_RTR_ABORT	(1 << 2)
#define CAN_RX_BUFFER_ENB	(1 << 3)
#define CAN_RX_RTR_REPLY_ENB	(1 << 4)
#define CAN_RX_INT_ENB		(1 << 5)
#define CAN_RX_LINK_ENB		(1 << 6)
#define CAN_RX_WPNL_ENB		(1 << 7)
#define CAN_RX_WPNH_ENB		(1 << 23)

#define CAN_INT_GLOBAL		(1 << 0)
#define CAN_INT_ARB_LOSS	(1 << 2)
#define CAN_INT_OVR_LOAD	(1 << 3)
#define CAN_INT_BIT_ERR		(1 << 4)
#define CAN_INT_STUFF_ERR	(1 << 5)
#define CAN_INT_ACK_ERR		(1 << 6)
#define CAN_INT_FORM_ERR	(1 << 7)
#define CAN_INT_CRC_ERR		(1 << 8)
#define CAN_INT_BUS_OFF		(1 << 9)
#define CAN_INT_RX_MSG_LOST	(1 << 10)
#define CAN_INT_TX_MSG		(1 << 11)
#define CAN_INT_RX_MSG		(1 << 12)
#define CAN_INT_RTR_MSG		(1 << 13)
#define CAN_INT_STUCK_AT_0	(1 << 14)
#define CAN_INT_SST_FAILURE	(1 << 15)

#define CAN_MODE_NORMAL		(0x01)
#define CAN_MODE_LISTEN_ONLY	(0x03)
#define CAN_MODE_EXT_LOOPBACK	(0x05)
#define CAN_MODE_INT_LOOPBACK	(0x07)
#define CAN_SRAM_TEST		(0x08)
#define CAN_SW_RESET		(0x10)

#define CMD_RUN_STOP_ENB	(1 << 0)

struct can_msg {
	uint32_t msgid;
	uint32_t datal;
	uint32_t datah;
	uint32_t flags;
};

struct mss_can_filter {
	uint32_t amr;
	uint32_t acr;
	uint16_t amcr_d_mask;
	uint16_t amcr_d_code;
};

struct can_txmsg {
	uint32_t txb;
	uint32_t msgid;
	uint32_t datal;
	uint32_t datah;
};

struct can_rxmsg {
	uint32_t rxb;
	uint32_t msgid;
	uint32_t datal;
	uint32_t datah;
	uint32_t amr;
	uint32_t acr;
	uint32_t amr_d;
	uint32_t acr_d;
};

struct can_device {
	uint32_t int_status;
	uint32_t int_enb;
	uint32_t rx_buf_status;
	uint32_t tx_buf_status;
	uint32_t err_status;
	uint32_t cmd;
	uint32_t cfg;
	uint32_t ecr;
	struct can_txmsg txmsg[NUM_TX_MAILBOXES];
	struct can_rxmsg rxmsg[NUM_RX_MAILBOXES];
};

#define SYSFS_PATH_LEN		(128)
#define ID_STR_LEN		(32)
#define UIO_DEVICE_PATH_LEN	(32)
#define NUM_UIO_DEVICES		(32)

uint32_t verbose = 0;
char uio_id_str[] = "mss_can0";
char sysfs_template[] = "/sys/class/uio/uio%d/%s";

int can_init(volatile struct can_device * dev, uint32_t bitrate)
{
	int i;

	for (i = 0; i < NUM_RX_MAILBOXES; i++) {
		dev->rxmsg[i].msgid = 0;
		dev->rxmsg[i].datal = 0;
		dev->rxmsg[i].datah = 0;
		dev->rxmsg[i].amr = 0;
		dev->rxmsg[i].acr = 0;
		dev->rxmsg[i].amr_d = 0;
		dev->rxmsg[i].acr_d = 0;
		dev->rxmsg[i].rxb = CAN_RX_WPNH_ENB | CAN_RX_WPNL_ENB | CAN_RX_BUFFER_ENB |
				    CAN_RX_INT_ENB;
	}

	dev->cfg = bitrate;
	return 0;
}

int can_set_mode(volatile struct can_device *dev, uint32_t mode)
{
	dev->cmd &= ~CMD_RUN_STOP_ENB;
	dev->cmd = mode;
	return 0;
}

int can_start(volatile struct can_device *dev)
{
	dev->int_enb = 0;
	dev->cmd |= CMD_RUN_STOP_ENB;
	dev->int_enb |= CAN_INT_GLOBAL | CAN_INT_RX_MSG;
	return 0;
}

int can_cfg_buf(volatile struct can_device *dev, struct mss_can_filter *filter)
{
	int i;

	for (i = 0; i < NUM_RX_MAILBOXES; i++) {
	dev->rxmsg[i].acr = filter->acr;
	dev->rxmsg[i].amr = filter->amr;
	dev->rxmsg[i].amr_d = filter->amcr_d_mask;
	dev->rxmsg[i].acr_d = filter->amcr_d_code;

	dev->rxmsg[i].rxb = CAN_RX_WPNH_ENB | CAN_RX_WPNL_ENB |
		CAN_RX_BUFFER_ENB | CAN_RX_INT_ENB | CAN_RX_LINK_ENB;

	/* Unset link flag for last buffer */
	if (i == NUM_RX_MAILBOXES-1)
		dev->rxmsg[i].rxb &= ~CAN_RX_LINK_ENB;
	}

	return 0;
}

int can_set_int_enb(volatile struct can_device *dev, uint32_t int_enb)
{
	dev->int_enb = int_enb;
	return 0;
}

uint32_t can_get_int_status(volatile struct can_device *dev)
{
	return dev->int_status;
}

int can_set_int_status(volatile struct can_device *dev, uint32_t status)
{
	dev->int_status = status;
	return 0;
}

uint32_t can_get_tx_buf_status(volatile struct can_device *dev)
{
	return dev->tx_buf_status;
}

int can_send_msg(volatile struct can_device *dev, struct can_msg *msg)
{
	int i;

	for (i = 0; i < NUM_TX_MAILBOXES; i++) {
	/* find first idle mailbox and use it */
		if ((can_get_tx_buf_status(dev) & (1 << i)) == 0) {
			dev->txmsg[i].msgid = msg->msgid;
			dev->txmsg[i].datal = msg->datal;
			dev->txmsg[i].datah = msg->datah;
			dev->txmsg[i].txb = (msg->flags | CAN_TX_REQ);
			return 0;
		}
	}

	return -1;
}

int can_get_msg_av(volatile struct can_device *dev)
{
	int i;

	for (i = 0; i < NUM_RX_MAILBOXES; i++) {
		if (dev->rxmsg[i].rxb & CAN_RX_MSGAV)
			return 1;
	}

	return 0;
}

int can_get_msg(volatile struct can_device *dev, struct can_msg *msg)
{
	int i;

	for (i = 0; i < NUM_RX_MAILBOXES; i++) {
		if (dev->rxmsg[i].rxb & CAN_RX_MSGAV) {
			msg->msgid = dev->rxmsg[i].msgid;
			msg->datal = dev->rxmsg[i].datal;
			msg->datah = dev->rxmsg[i].datah;
			msg->flags = dev->rxmsg[i].rxb;
			/* ack the message */
			dev->rxmsg[i].rxb |= CAN_RX_MSGAV;
			return 1;
		}
	}

	return 0;
}

void canmsgtostr(struct can_msg *msg, char *rxbuf)
{
	memcpy(rxbuf, &msg->datal, 4);
	memcpy(rxbuf+4, &msg->datah, 4);
}

void print_ints(uint32_t val)
{
	if (val & CAN_INT_GLOBAL)
		printf("CAN_INT_GLOBAL\n");
	if (val & CAN_INT_ARB_LOSS)
		printf("CAN_INT_ARB_LOSS\n");
	if (val & CAN_INT_OVR_LOAD)
		printf("CAN_INT_OVR_LOAD\n");
	if (val & CAN_INT_BIT_ERR)
		printf("CAN_INT_BIT_ERR\n");
	if (val & CAN_INT_STUFF_ERR)
		printf("CAN_INT_STUFF_ERR\n");
	if (val & CAN_INT_ACK_ERR)
		printf("CAN_INT_ACK_ERR\n");
	if (val & CAN_INT_FORM_ERR)
		printf("CAN_INT_FORM_ERR\n");
	if (val & CAN_INT_CRC_ERR)
		printf("CAN_INT_CRC_ERR\n");
	if (val & CAN_INT_BUS_OFF)
		printf("CAN_INT_BUS_OFF\n");
	if (val & CAN_INT_RX_MSG_LOST)
		printf("CAN_INT_RX_MSG_LOST\n");
	if (val & CAN_INT_TX_MSG)
		printf("CAN_INT_TX_MSG\n");
	if (val & CAN_INT_RX_MSG)
		printf("CAN_INT_RX_MSG\n");
	if (val & CAN_INT_RTR_MSG)
		printf("CAN_INT_RTR_MSG\n");
	if (val & CAN_INT_STUCK_AT_0)
		printf("CAN_INT_STUCK_AT_0\n");
	if (val & CAN_INT_SST_FAILURE)
		printf("CAN_INT_SST_FAILURE\n");
	if (val & 0xffff0002)
		printf("RESERVED\n");
}

void handle_int(volatile struct can_device *dev, uint32_t val)
{
	char rxbuf[8] = "";
	struct can_msg msg;
	int ret;

	if (verbose)
		print_ints(val);

	while ((ret = can_get_msg_av(dev))) {
		ret = can_get_msg(dev, &msg);
		canmsgtostr(&msg, rxbuf);
		printf("received msg: '%s'\n", rxbuf);
	}

}

int wait_for_interrupt(int fd, volatile struct can_device * dev)
{
	 struct pollfd fds = {
		 .fd = fd,
	 	.events = POLLIN,
	 };
	 int reenable = 1;
	 uint32_t val;
	 int ret = poll(&fds, 1, 100);

	 if (ret >= 1) {
		 read(fd, (void *)&reenable, sizeof(int));
	 	val = can_get_int_status(dev);
	 	handle_int(dev, val);
	 	can_set_int_status(dev, val);
	 	write(fd, (void *)&reenable, sizeof(int));
	 }

	 return ret;
}

int get_uio_device(char * id)
{
	FILE *fp;
	int i;
	int len;
	char file_id[ID_STR_LEN];
	char sysfs_path[SYSFS_PATH_LEN];

	for (i = 0; i < NUM_UIO_DEVICES; i++) {
		snprintf(sysfs_path, SYSFS_PATH_LEN, sysfs_template, i, "/name");
		fp = fopen(sysfs_path, "r");
		if (fp == NULL)
			break;

		fscanf(fp, "%32s", file_id);
		len = strlen(id);
		if (len > ID_STR_LEN-1)
			len = ID_STR_LEN-1;
		if (strncmp(file_id, id, len) == 0) {
			return i;
		}
	}

	return -1;
}

uint32_t get_memory_size(char *sysfs_path, char *uio_device)
{
	FILE *fp;
	uint32_t sz;

	/* 
	 * open the file the describes the memory range size.
	 * this is set up by the reg property of the node in the device tree
	 */
	fp = fopen(sysfs_path, "r");
	if (fp == NULL) {
		fprintf(stderr, "unable to determine size for %s\n", uio_device);
		exit(0);
	}

	fscanf(fp, "0x%016X", &sz);
	fclose(fp);
	return sz;
}

int main(int argc, char *argv[])
{
	int ret = 0;
	int uiofd;
	char uio_device[UIO_DEVICE_PATH_LEN];
	char sysfs_path[SYSFS_PATH_LEN];
	uint32_t mmap_size;
	volatile struct can_device * dev;
	struct mss_can_filter filter;
	struct can_msg msg;
	uint32_t bitrate = CAN_SPEED_8M_1M;
	uint32_t int_enb = CAN_INT_ACK_ERR | CAN_INT_TX_MSG | CAN_INT_GLOBAL |
		CAN_INT_RX_MSG | CAN_INT_BUS_OFF | CAN_INT_BIT_ERR |
		CAN_INT_OVR_LOAD | CAN_INT_FORM_ERR | CAN_INT_CRC_ERR |
		CAN_INT_RX_MSG_LOST | CAN_INT_RTR_MSG | CAN_INT_STUCK_AT_0 |
		CAN_INT_STUFF_ERR | CAN_INT_SST_FAILURE | CAN_INT_ARB_LOSS;
	uint8_t txmsgs[NUM_MSGS][8] = {
		"example",
	};
	uint8_t n_msgs = NUM_MSGS;
	int i;
	int index;

	printf("locating device for %s\n", uio_id_str);
        index = get_uio_device(uio_id_str);
	if (index < 0) {
		fprintf(stderr, "can't locate uio device for %s\n", uio_id_str);
		return -1;
	}

	snprintf(uio_device, UIO_DEVICE_PATH_LEN, "/dev/uio%d", index);
	printf("located %s\n", uio_device);

	uiofd = open(uio_device, O_RDWR);
	if(uiofd < 0) {
		fprintf(stderr, "cannot open %s: %s\n", uio_device, strerror(errno));
		return -1;
	} else {
		printf("opened %s (r,w)\n", uio_device);
	}

	snprintf(sysfs_path, SYSFS_PATH_LEN, sysfs_template, index, "maps/map0/size");
	mmap_size = get_memory_size(sysfs_path, uio_device);
	if (mmap_size == 0) {
		fprintf(stderr, "bad memory size for %s\n", uio_device);
		return -1;
	}

	dev = mmap(NULL, mmap_size, PROT_READ|PROT_WRITE, MAP_SHARED, uiofd, 0);
	if (dev == MAP_FAILED) {
		fprintf(stderr, "cannot mmap %s: %s\n", uio_device, strerror(errno));
		return -1;
	} else {
		printf("mapped 0x%x bytes for %s\n", mmap_size, uio_device);
	}

	printf("setting can device at %s into loopback mode\n", uio_device);
	can_init(dev, bitrate);
	can_set_mode(dev, CAN_MODE_INT_LOOPBACK);
	can_start(dev);

	/* configure for receive */
	filter.acr = 0;
	filter.amr = 0xffffffff;
	filter.amcr_d_mask = 0xffff;
	filter.amcr_d_code = 0x0;
	can_cfg_buf(dev, &filter);

	/* enable interrupts */
	can_set_int_enb(dev, int_enb);

	msg.msgid = 0x120 << 3;
	for(i = 0; i < n_msgs; i++) {
		memcpy(&msg.datal, &txmsgs[i][0], 4);
		memcpy(&msg.datah, &txmsgs[i][4], 4);
		msg.flags = CAN_TX_INT_ENB | CAN_TX_WPNH_ENB | CAN_TX_WPNL_ENB;
		msg.flags |= 0x8 << 16;
		ret = can_send_msg(dev, &msg);
		if (ret) {
			fprintf(stderr, "Failed to send message\n");
			exit(0);
		} else {
			printf("sent msg: '%s'\n", &txmsgs[i][0]);
		}
	}

	while(wait_for_interrupt(uiofd, dev))
		;

	ret = munmap((void *)dev, mmap_size);
	printf("unmapped %s\n", uio_device);
	ret = close(uiofd);
	printf("closed %s\n", uio_device);
	return ret;
}
