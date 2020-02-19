RISCV_TARGET=l2ways

TARGET:=$(RISCV_TARGET)
$(RISCV_TARGET):


CORE_CFLAGS+=-mcmodel=medany


l2ways.o: l2ways.c
	$(CC) -c -o $@ $< 


clean: 
	rm -f $o
	echo Clean done
