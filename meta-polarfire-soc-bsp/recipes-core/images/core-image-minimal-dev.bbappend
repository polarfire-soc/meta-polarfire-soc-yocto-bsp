
EXTRA_IMAGE_FEATURES += " \
    tools-debug \
    tools-sdk \
"

IMAGE_INSTALL = "\
    ${CORE_IMAGE_EXTRA_INSTALL} \
    can-utils \
    cmake \
    devmem2 \
    dtc \
    expect \
    glib-2.0 \
    htop \
    i2c-tools \
    iperf3 \
    iproute2 \
    kernel-modules \
    libgpiod \
    libgpiod-tools \
    libudev \
    mtd-utils \
    mtd-utils-ubifs \
    packagegroup-base \
    packagegroup-core-base-utils \
    polarfire-soc-linux-examples \
    python3 \
    python3-pip \
    python3-werkzeug \
    rng-tools \
    rsync \
    sqlite3 \
    zip \
"
