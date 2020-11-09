
EXTRA_IMAGE_FEATURES += " \
      tools-debug \      
      tools-sdk"

IMAGE_INSTALL = "\
    pci_utils \
    pfsocapps \
    collectd \
    libgpiod \
    libgpiod-tools \
    libgpiod-dev \
    i2c-tools \
    vim vim-vimrc \
    net-tools \
    dhcp-client \
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
    ${CORE_IMAGE_EXTRA_INSTALL} \
    "



