SUMMARY = "Point-to-Point Protocol (PPP) support"
DESCRIPTION = "ppp (Paul's PPP Package) is an open source package which implements \
the Point-to-Point Protocol (PPP) on Linux and Solaris systems."
SECTION = "console/network"
HOMEPAGE = "http://samba.org/ppp/"
BUGTRACKER = "http://ppp.samba.org/cgi-bin/ppp-bugs"
DEPENDS = "libpcap"
LICENSE = "BSD & GPLv2+ & LGPLv2+ & PD"
LIC_FILES_CHKSUM = "file://pppd/ccp.c;beginline=1;endline=29;md5=e2c43fe6e81ff77d87dc9c290a424dea \
                    file://pppd/plugins/passprompt.c;beginline=1;endline=10;md5=3bcbcdbf0e369c9a3e0b8c8275b065d8 \
                    file://pppd/tdb.c;beginline=1;endline=27;md5=4ca3a9991b011038d085d6675ae7c4e6 \
                    file://chat/chat.c;beginline=1;endline=15;md5=0d374b8545ee5c62d7aff1acbd38add2"
PR = "r6"

SRC_URI = "http://ppp.samba.org/ftp/ppp/ppp-${PV}.tar.gz \
           file://makefile.patch \
           file://cifdefroute.patch \
           file://pppd-resolv-varrun.patch \
           file://enable-ipv6.patch \
           file://makefile-remove-hard-usr-reference.patch \
           file://update_if_pppol2tp.patch \
           file://pon \
           file://poff \
           file://init \
           file://ip-up \
           file://ip-down \
           file://08setupdns \
           file://92removedns \
           file://copts.patch \
           file://pap \
           file://ppp_on_boot \
           file://provider "

SRC_URI[md5sum] = "4621bc56167b6953ec4071043fe0ec57"
SRC_URI[sha256sum] = "43317afec9299f9920b96f840414c977f0385410202d48e56d2fdb8230003505"

inherit autotools

TARGET_CC_ARCH += " ${LDFLAGS}"
EXTRA_OEMAKE = "STRIPPROG=${STRIP} MANDIR=${D}${datadir}/man/man8 INCDIR=${D}${includedir} LIBDIR=${D}${libdir}/pppd/${PV} BINDIR=${D}${sbindir}"
EXTRA_OECONF = "--disable-strip"

# Package Makefile computes CFLAGS, referencing COPTS.
# Typically hard-coded to '-O2 -g' in the Makefile's.
#
EXTRA_OEMAKE += ' COPTS="${CFLAGS}"'

do_install_append () {
	make install-etcppp ETCDIR=${D}/${sysconfdir}/ppp
	mkdir -p ${D}${bindir}/ ${D}${sysconfdir}/init.d
	mkdir -p ${D}${sysconfdir}/ppp/ip-up.d/
	mkdir -p ${D}${sysconfdir}/ppp/ip-down.d/
	install -m 0755 ${WORKDIR}/pon ${D}${bindir}/pon
	install -m 0755 ${WORKDIR}/poff ${D}${bindir}/poff
	install -m 0755 ${WORKDIR}/init ${D}${sysconfdir}/init.d/ppp
	install -m 0755 ${WORKDIR}/ip-up ${D}${sysconfdir}/ppp/
	install -m 0755 ${WORKDIR}/ip-down ${D}${sysconfdir}/ppp/
	install -m 0755 ${WORKDIR}/08setupdns ${D}${sysconfdir}/ppp/ip-up.d/
	install -m 0755 ${WORKDIR}/92removedns ${D}${sysconfdir}/ppp/ip-down.d/
	mkdir -p ${D}${sysconfdir}/chatscripts
	mkdir -p ${D}${sysconfdir}/ppp/peers
	install -m 0755 ${WORKDIR}/pap ${D}${sysconfdir}/chatscripts
	install -m 0755 ${WORKDIR}/ppp_on_boot ${D}${sysconfdir}/ppp/ppp_on_boot
	install -m 0755 ${WORKDIR}/provider ${D}${sysconfdir}/ppp/peers/provider
	rm -rf ${D}/${mandir}/man8/man8
	chmod u+s ${D}${sbindir}/pppd
}

CONFFILES_${PN} = "${sysconfdir}/ppp/pap-secrets ${sysconfdir}/ppp/chap-secrets ${sysconfdir}/ppp/options"
PACKAGES =+ "${PN}-oa ${PN}-oe ${PN}-radius ${PN}-winbind ${PN}-minconn ${PN}-password ${PN}-l2tp ${PN}-tools"
FILES_${PN}        = "${sysconfdir} ${bindir} ${sbindir}/chat ${sbindir}/pppd"
FILES_${PN}-dbg += "${libdir}/pppd/${PV}/.debug"
FILES_${PN}-oa       = "${libdir}/pppd/${PV}/pppoatm.so"
FILES_${PN}-oe       = "${sbindir}/pppoe-discovery ${libdir}/pppd/${PV}/rp-pppoe.so"
FILES_${PN}-radius   = "${libdir}/pppd/${PV}/radius.so ${libdir}/pppd/${PV}/radattr.so ${libdir}/pppd/${PV}/radrealms.so"
FILES_${PN}-winbind  = "${libdir}/pppd/${PV}/winbind.so"
FILES_${PN}-minconn  = "${libdir}/pppd/${PV}/minconn.so"
FILES_${PN}-password = "${libdir}/pppd/${PV}/pass*.so"
FILES_${PN}-l2tp     = "${libdir}/pppd/${PV}/*l2tp.so"
FILES_${PN}-tools    = "${sbindir}/pppstats ${sbindir}/pppdump"
DESCRIPTION_${PN}-oa       = "Plugin for PPP needed for PPP-over-ATM"
DESCRIPTION_${PN}-oe       = "Plugin for PPP needed for PPP-over-Ethernet"
DESCRIPTION_${PN}-radius   = "Plugin for PPP that are related to RADIUS"
DESCRIPTION_${PN}-winbind  = "Plugin for PPP to authenticate against Samba or Windows"
DESCRIPTION_${PN}-minconn  = "Plugin for PPP to specify a minimum connect time before the idle timeout applies"
DESCRIPTION_${PN}-password = "Plugin for PPP to get passwords via a pipe"
DESCRIPTION_${PN}-l2tp     = "Plugin for PPP for l2tp support"
DESCRIPTION_${PN}-tools    = "The pppdump and pppstats utitilities"
