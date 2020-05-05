#!/bin/csh
# xv wrapper that uses fits_to_targa to percentile scale the FITS image before display.
# assumes fits_to_targa is installed in bin_dir on the CcsGUI machine.
# xv_wrapper <FITS Image>
if($#argv < 1) then
    echo "xv_wrapper <FITS Image>"
    exit 1
endif
set bin_dir = "~dev/bin/ccd/misc/${HOSTTYPE}"
set min_percentile = 5.0
set max_percentile = 95.0
set filename_fits = $1
set filename_tga = `echo ${filename_fits} | sed "s/fits/tga/g"`
${bin_dir}/fits_to_targa -i ${filename_fits} -o ${filename_tga} -p ${min_percentile} ${max_percentile}
#cjpeg -quality 90 ${filename_tga} > ${filename_jpg}
xv ${filename_tga}
rm ${filename_tga}
