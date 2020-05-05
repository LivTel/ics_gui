#!/bin/csh
# list_accelerators.csh <filename list>
# List Swing Accelerator keys defined in Java Source files.
# Looks for Java source of the type:
# menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q,ActionEvent.CTRL_MASK));
# end prints out:
# CTRL Q
foreach file ( ${argv[*]} )
    echo "Processing file:"$file
    cat $file | grep setAccelerator | sed "s/.*KeyEvent.VK_\(.*\),ActionEvent.\(.*\)_MASK.*/\2 \1/g"
end
