# Finalize sudo accesses for AUTHORIZED_USER_NAME
AUTHORIZED_USER_NAME ?= "daemon"

do_install:append:mpfs-video-kit () {
    # Add sudo accesses for user.
    install -d -m 0710 "${D}/etc/sudoers.d"

    echo 'daemon ALL=(ALL:ALL) NOPASSWD: /srv/www/h264/ffmpeg.sh
    daemon ALL=(ALL:ALL) NOPASSWD: /srv/www/h264/stop.sh
    daemon ALL=(ALL:ALL) NOPASSWD: /srv/www/h264/update.sh
    daemon ALL=(ALL:ALL) NOPASSWD: /usr/bin/ffmpeg'  > "${D}/etc/sudoers.d/0001_${AUTHORIZED_USER_NAME}"

    chmod 0644 "${D}/etc/sudoers.d/0001_${AUTHORIZED_USER_NAME}"
}

FILES_${PN}:mpfs-video-kit +=  "/etc/sudoers.d \
               /etc/sudoers.d/0001_${AUTHORIZED_USER_NAME}"
