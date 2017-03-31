#!/bin/bash
#
# Script for convering source icon images from XCF (GIMP) to PNG format.
# Uses ImageMagic's convert utility.

dst='./converted'

function convertXcf {
    image=$1
    echo "Converting ${image}"
    convert -alpha Set -background none -layers merge "${image}.xcf" "${dst}/${image}@tmp.png"
    convert -resize 32x32 "${dst}/${image}@tmp.png" "${dst}/${image}@2x.png"
    convert -resize 16x16 "${dst}/${image}@tmp.png" "${dst}/${image}.png"
    rm "${dst}/${image}@tmp.png"
}


rm -rvf $dst
mkdir $dst

convertXcf 'proto'
convertXcf 'message'
convertXcf 'enum'
convertXcf 'service'
convertXcf 'field'
convertXcf 'constant'
convertXcf 'rpc'
