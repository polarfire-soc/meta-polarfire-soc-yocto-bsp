DESCRIPTION = "Microchip MPFS Development CLI Linux image"

inherit image-buildinfo core-image extrausers

IMAGE_FEATURES += "splash ssh-server-openssh \
                   tools-debug tools-sdk debug-tweaks \
                   dev-pkgs dbg-pkgs \
                  "

IMAGE_INSTALL = "\
    packagegroup-core-boot \
    packagegroup-core-full-cmdline \
    perl-modules \
    alsa-utils \
    i2c-tools \
    screen \
    vim vim-vimrc \
    dhcp-client \
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
    pciutils \
    usbutils \
    mtd-utils \
    sysfsutils \
    htop \
    iw \
    python3 \
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
    ntp ntpdate ntp-utils \
    linux-firmware \
    libsodium \
    sqlite3 \
    tar \
    wget \
    zip \
    unzip \
    rsync \
    kernel-modules kernel-devsrc kernel-dev \
    ${CORE_IMAGE_EXTRA_INSTALL} \
    "



