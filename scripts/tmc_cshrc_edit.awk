#!/bin/awk -f
BEGIN{
  copy = 1
  nextcopy = 0
} 
/\# \<icsgui_install\:start\>/ { copy = 0 }
/\# \<icsgui_install\:end\>/ { nextcopy = 1 }
 { 
   if(copy == 1) {
     print $0
   }
   if(nextcopy == 1) {
     nextcopy = 0
     copy = 1 
   }
}
