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
    fswebcam \
    python3-asyncua \
    openssl \
    openssl-engines \
    cryptodev-module \
    libkcapi \
    libnl \
    "

IMAGE_INSTALL:append:icicle-kit-es = " \
    python3-asyncua \
    "
