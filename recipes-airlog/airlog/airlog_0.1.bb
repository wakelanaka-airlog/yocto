SUMMARY = "AirLog - Air Quality Monitoring System"
DESCRIPTION = "Layer to measure co2, ppm and humidity"
LICENSE = "CLOSED"

PV = "1.0"


SRC_URI ="git://git@github.com/wakelanaka-airlog/dashboard.git;protocol=ssh;branch=main \
           file://airlog.service \
"

SRCREV = "0b958157441bb6c4ef41329b285e3900dfcf96c6"

S = "${WORKDIR}/git"

IMAGE_INSTALL:append = " \
    airlog \
    qtbase-plugins \
"

inherit pkgconfig qt6-cmake systemd
PACKAGECONFIG ??= ""
PACKAGECONFIG[test] = "-DBUILD_TESTING=ON,-DBUILD_TESTING=OFF,googletest"

OECMAKE_TARGET = "all"
EXTRA_OECMAKE = ""
SYSTEMD_SERVICE:${PN} = "airlog.service"

DEPENDS += " \
    qtbase \
    qtdeclarative \
    wayland \
    wayland-protocols \
    qtdeclarative-native \
    qtcharts \
    mosquitto \
"

RDEPENDS:${PN} += " \
    qtwayland \
    qtcharts-qmlplugins \
    qtdeclarative-qmlplugins \
    ttf-jetbrains-mono-nerd-font \
"

do_install:append() {
    install -d ${D}${systemd_system_unitdir}
    install -m 0644 ${WORKDIR}/airlog.service ${D}${systemd_system_unitdir}/

    install -d -m 0700 ${D}${sysconfdir}/airlog
    # Empty placeholder, filled in on-device with real MQTT_*/RESTAPI_* values
    # (see dashboard/.env.example) - never shipped with real credentials.
    install -m 0600 /dev/null ${D}${sysconfdir}/airlog/env
}

FILES:${PN} += "${localstatedir}/lib/airlog ${sysconfdir}/airlog ${libdir}/libdashboard_app_module.so"
CONFFILES:${PN} += "${sysconfdir}/airlog/env"

# dashboard_app_module is a private QML plugin, not a public library other
# recipes link against - nothing legitimate for -dev to claim (no headers,
# no pkgconfig/cmake export files installed). Left as default, -dev's
# `${libdir}/lib*.so` glob claims the unversioned .so before FILES:${PN}
# above ever gets a chance (packages are matched in PACKAGES order, -dev
# before PN), which fails do_package_qa's dev-deps/dev-elf checks.
FILES:${PN}-dev = ""
