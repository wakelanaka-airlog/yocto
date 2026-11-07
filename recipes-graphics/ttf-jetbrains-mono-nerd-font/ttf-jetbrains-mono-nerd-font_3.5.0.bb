SUMMARY = "JetBrains Mono Nerd Font (Mono variant), patched with icon glyphs"
DESCRIPTION = "The fixed-width 'Mono' variant of JetBrains Mono, patched by \
the Nerd Fonts project - used by dashboard's Theme.fontMono (see \
dashboard/src/Theme.qml)."
HOMEPAGE = "https://github.com/ryanoasis/nerd-fonts"
LICENSE = "OFL-1.1"
LIC_FILES_CHKSUM = "file://README.md;md5=d54ec47108c8fd9465fe5dafb1a7ccc4"

SRC_URI = "https://github.com/ryanoasis/nerd-fonts/releases/download/v${PV}/JetBrainsMono.tar.xz"
SRC_URI[sha256sum] = "0227b220360a6f819b9ead92343e8112b34733054782561af50cfba1e8afab63"

S = "${WORKDIR}"

# Font-only package: no compiler/toolchain needed.
INHIBIT_DEFAULT_DEPS = "1"

inherit allarch fontcache

RDEPENDS:${PN} += "fontconfig"

# The upstream release tarball bundles every weight of every variant
# (Mono/Propo/NL, ~120 files) - only install the "Mono" variant (matches
# QML's fixed-width font.family exactly) at the weights dashboard/src/*.qml
# actually requests (Regular, Font.DemiBold, Font.Bold).
do_install() {
    install -d ${D}${datadir}/fonts/truetype/
    install -m 0644 ${S}/JetBrainsMonoNerdFontMono-Regular.ttf ${D}${datadir}/fonts/truetype/
    install -m 0644 ${S}/JetBrainsMonoNerdFontMono-SemiBold.ttf ${D}${datadir}/fonts/truetype/
    install -m 0644 ${S}/JetBrainsMonoNerdFontMono-Bold.ttf ${D}${datadir}/fonts/truetype/
}

FILES:${PN} = "${datadir}/fonts/truetype/*"
