
EXTRA_IMAGE_FEATURES += " \
      tools-debug \      
      tools-sdk"

IMAGE_INSTALL = "\
    ncurses-tools \
    expect \
    rsync \
    rng-tools \
    iperf3 \
    devmem2 \
    can-utils \
    usbutils \
    pciutils \
    polarfire-soc-linux-examples \
    dt-overlay-mchp \
    libgpiod \
    libgpiod-tools \
    libgpiod-dev \
    i2c-tools \
    vim vim-vimrc \
    net-tools \
    htop \
    iw \
    python3 \
    python3-pip \
    python3-flask \
    python3-flask-dev \
    python3-werkzeug \
    libudev \
    glib-2.0 \
    sqlite3 \
    dtc \
    cmake \
    tar \
    wget \
    zip \
    mtd-utils \
    mtd-utils-ubifs \
    kernel-modules \
    ${CORE_IMAGE_EXTRA_INSTALL} \
    "

IMAGE_INSTALL:append:mpfs-video-kit = " \
    ffmpeg \
    apache2 \
    php \
    php-cli \
    php-fpm \
    php-cgi \
    php-modphp \
    sudo \
    v4l2-start-service \
    v4l-utils \
    videokit-webdemo \
    "

