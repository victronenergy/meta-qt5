require python-pyqt5.inc

inherit python3native python3-dir

DEPENDS += "sip sip-native python"

RDEPENDS_${PN} += "python-core python-sip"

