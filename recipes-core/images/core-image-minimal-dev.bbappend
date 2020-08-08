
EXTRA_IMAGE_FEATURES += " \
      dbg-pkgs \      
      debug-tweaks \  
      tools-sdk \     
      dev-pkgs"


IMAGE_INSTALL = "\
    iiohttpserver \
    collectd \
    libgpiod \
    libgpiod-tools \
    libgpiod-dev \
    i2c-tools \
    vim vim-vimrc \
    net-tools \
    dhcp-client \
    glibc-dev \
    sysfsutils \
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
    tar \
    wget \
    zip \
    unzip \
    rsync \
    dtc \
    cmake \
    ${CORE_IMAGE_EXTRA_INSTALL} \
    "



