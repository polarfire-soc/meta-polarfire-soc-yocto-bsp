
EXTRA_IMAGE_FEATURES += " \
      tools-debug \      
      tools-sdk"

IMAGE_INSTALL = "\
    packagegroup-core-base-utils \
    expect \
    rsync \
    rng-tools \
    iperf3 \
    devmem2 \
    can-utils \
    polarfire-soc-linux-examples \
    dt-overlay-mchp \
    libgpiod \
    libgpiod-tools \
    i2c-tools \
    htop \
    python3 \
    python3-pip \
    python3-werkzeug \
    libudev \
    glib-2.0 \
    sqlite3 \
    dtc \
    cmake \
    mtd-utils \
    mtd-utils-ubifs \
    kernel-modules \
    ${CORE_IMAGE_EXTRA_INSTALL} \
    "



