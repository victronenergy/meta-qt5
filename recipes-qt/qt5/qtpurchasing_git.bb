require qt5.inc
require qt5-git.inc

HOMEPAGE = "http://www.qt.io"
LICENSE = "Apache-2.0 & BSD & ( LGPL-3.0 | GPL-3.0 | The-Qt-Company-Commercial )"
LIC_FILES_CHKSUM = " \
    file://LICENSE.LGPLv3;md5=b8c75190712063cde04e1f41b6fdad98 \
    file://LICENSE.GPLv3;md5=40f9bf30e783ddc201497165dfb32afb \
"

DEPENDS += "qtbase qtdeclarative"

SRCREV = "e5ae8515422fd0cc7cf9aa91c8b0c38fa53fd9a9"
