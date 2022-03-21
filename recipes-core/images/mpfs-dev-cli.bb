DESCRIPTION = "Microchip MPFS Development CLI Linux image"

inherit image-buildinfo core-image extrausers pypi setuptools3

IMAGE_FEATURES += " ssh-server-openssh \
                   tools-debug tools-sdk debug-tweaks package-management \
                   dev-pkgs dbg-pkgs \
                  "

IMAGE_INSTALL = "\
    iperf3 \
    devmem2 \
    packagegroup-core-boot \
    packagegroup-core-full-cmdline \
    polarfire-soc-linux-examples \
    coremarkpro \
    libgpiod \
    libgpiod-tools \
    libgpiod-dev \
    yavta \
    libuvc \
    gstd \
    gstreamer1.0-plugins-good \
    v4l-utils \
    perl-modules \
    alsa-utils \
    i2c-tools \
    screen \
    vim vim-vimrc \
    dhcpcd \
    nbd-client \
    mpfr-dev \
    gmp-dev \
    libmpc-dev \    
    zlib-dev \
    flex \
    bison \
    dejagnu \
    gettext \
    texinfo \
    procps \
    glibc-dev \
    elfutils \
    elfutils-dev \
    can-utils \
    pciutils \
    usbutils \
    sysfsutils \
    htop \
    iw \
    python3 \
    python3-pip \
    python3-flask \
    python3-flask-dev \
    python3-werkzeug \
    git \
    swig \
    orc \
    libudev \
    glib-2.0 \
    tcpdump \
    iw \
    libudev \
    nano \
    nfs-utils-client \
    cifs-utils \
    openssh-sftp \
    openssh-sftp-server \
    procps \
    libsodium \
    tar \
    wget \
    zip \
    unzip \
    rsync \
    dtc \
    cmake \
    kernel-modules kernel-dev \
    ${CORE_IMAGE_EXTRA_INSTALL} \
    "



