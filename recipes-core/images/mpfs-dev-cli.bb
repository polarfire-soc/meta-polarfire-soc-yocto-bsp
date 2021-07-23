DESCRIPTION = "Microchip MPFS Development CLI Linux image"

inherit image-buildinfo core-image extrausers pypi setuptools3

IMAGE_FEATURES += " ssh-server-openssh \
                   tools-debug tools-sdk debug-tweaks package-management \
                   dev-pkgs dbg-pkgs \
                  "

IMAGE_INSTALL = "\
    iperf3 \
    packagegroup-core-boot \
    packagegroup-core-full-cmdline \
    pfsocapps \
    coremarkpro \
    collectd \
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
    mtd-utils \
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
    boost \
    orc \
    libudev \
    glib-2.0 \
    evtest devmem2 iperf3 memtester lmbench \
    tcpdump \
    iw \
    libudev \
    nano \
    nfs-utils-client \
    cifs-utils \
    openssh-sftp \
    openssh-sftp-server \
    procps \
    protobuf \
    libsodium \
    sqlite3 \
    tar \
    wget \
    zip \
    unzip \
    rsync \
    dtc \
    cmake \
    kernel-modules kernel-devsrc kernel-dev \
    ${CORE_IMAGE_EXTRA_INSTALL} \
    "



