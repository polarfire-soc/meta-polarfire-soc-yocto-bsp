DESCRIPTION = "Microchip MPFS Development CLI Linux image"

inherit image-buildinfo core-image extrausers
inherit pypi setuptools3

IMAGE_FEATURES += " \
    dbg-pkgs \
    debug-tweaks \
    dev-pkgs \
    package-management \
    ssh-server-openssh \
    tools-debug \
    tools-sdk \
"

IMAGE_INSTALL = "\
    alsa-utils \
    bison \
    can-utils \
    cifs-utils \
    cmake \
    coremarkpro \
    dejagnu \
    devmem2 \
    dtc \
    elfutils \
    elfutils-dev \
    flex \
    gettext \
    git \
    glib-2.0 \
    glibc-dev \
    gmp-dev \
    gstd \
    gstreamer1.0-plugins-good \
    htop \
    i2c-tools \
    iperf3 \
    iproute2 \
    iw \
    iw \
    kernel-modules kernel-dev \
    libgpiod \
    libgpiod-dev \
    libgpiod-tools \
    libmpc-dev \    
    libsodium \
    libudev \
    libudev \
    libuvc \
    mpfr-dev \
    nano \
    nbd-client \
    nfs-utils-client \
    openssh-sftp \
    openssh-sftp-server \
    orc \
    packagegroup-base \
    packagegroup-core-boot \
    packagegroup-core-full-cmdline \
    perl-modules \
    polarfire-soc-linux-examples \
    procps \
    procps \
    python3 \
    python3-pip \
    python3-werkzeug \
    rsync \
    rsync \
    screen \
    swig \
    sysfsutils \
    tar \
    tcpdump \
    texinfo \
    unzip \
    v4l-utils \
    vim vim-vimrc \
    wget \
    yavta \
    zip \
    zlib-dev \
    ${CORE_IMAGE_EXTRA_INSTALL} \
"

