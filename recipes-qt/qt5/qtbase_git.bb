require qtbase_git.inc

do_install_append() {
    # Avoid qmake error "Cannot read [...]/usr/lib/qt5/mkspecs/oe-device-extra.pri: No such file or directory"
    touch ${D}/${OE_QMAKE_PATH_QT_ARCHDATA}/mkspecs/oe-device-extra.pri

    # Replace host paths with qmake built-in properties
    sed -i -e 's|${STAGING_DIR_NATIVE}|$$[QT_HOST_PREFIX/get]|g' \
        -e 's|${STAGING_DIR_HOST}|$$[QT_SYSROOT]|g' \
        ${D}/${OE_QMAKE_PATH_QT_ARCHDATA}/mkspecs/*.pri

    # Update the mkspecs to include the default OE toolchain config for the target
    conf=${D}/${OE_QMAKE_PATH_QT_ARCHDATA}/mkspecs/${XPLATFORM}/qmake.conf

    # qmake already knows the sysroot, see above $$[QT_SYSROOT], so remove the hardcoded sysroot
    OE_QMAKE_CC_NO_SYSROOT=$(echo ${OE_QMAKE_CC} | sed -e 's!--sysroot=[^ ]*!!g')
    OE_QMAKE_CXX_NO_SYSROOT=$(echo ${OE_QMAKE_CXX} | sed -e 's!--sysroot=[^ ]*!!g')
    OE_QMAKE_LINK_NO_SYSROOT=$(echo ${OE_QMAKE_LINK} | sed -e 's!--sysroot=[^ ]*!!g')

    echo "" >> $conf
    echo "# default compiler options which can be overwritten from the environment" >> $conf
    echo "isEmpty(QMAKE_AR): QMAKE_AR = ${OE_QMAKE_AR} cqs" >> $conf
    echo "isEmpty(QMAKE_CC): QMAKE_CC = $OE_QMAKE_CC_NO_SYSROOT" >> $conf
    echo "isEmpty(QMAKE_CFLAGS): QMAKE_CFLAGS = ${OE_QMAKE_CFLAGS}" >> $conf
    echo "isEmpty(QMAKE_CXX): QMAKE_CXX = $OE_QMAKE_CXX_NO_SYSROOT" >> $conf
    echo "isEmpty(QMAKE_CXXFLAGS): QMAKE_CXXFLAGS = ${OE_QMAKE_CXXFLAGS}" >> $conf
    echo "isEmpty(QMAKE_LINK): QMAKE_LINK = $OE_QMAKE_LINK_NO_SYSROOT" >> $conf
    echo "isEmpty(QMAKE_LINK_SHLIB): QMAKE_LINK_SHLIB = $OE_QMAKE_LINK_NO_SYSROOT" >> $conf
    echo "isEmpty(QMAKE_LINK_C): QMAKE_LINK_C = $OE_QMAKE_LINK_NO_SYSROOT" >> $conf
    echo "isEmpty(QMAKE_LINK_C_SHLIB): QMAKE_LINK_C_SHLIB = $OE_QMAKE_LINK_NO_SYSROOT" >> $conf
    echo "isEmpty(QMAKE_LFLAGS): QMAKE_LFLAGS = ${OE_QMAKE_LDFLAGS}" >> $conf
    echo "isEmpty(QMAKE_STRIP): QMAKE_STRIP = ${TARGET_PREFIX}strip" >> $conf

    generate_target_qt_config_file ${D}${OE_QMAKE_PATH_BINS}/qt.conf

    # Fix up absolute paths in scripts
    sed -i -e '1s,#!/usr/bin/python,#! ${USRBINPATH}/env python,' \
        ${D}${OE_QMAKE_PATH_QT_ARCHDATA}/mkspecs/features/uikit/devices.py
}
