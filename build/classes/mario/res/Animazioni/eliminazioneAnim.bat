@ECHO OFF
title first batch program

ECHO "Script per la cancellazione deglle animazioni"

del /Q Tile\\unlockable\\*.anim
del /Q Tile\\terrain\\dark\\*.anim
del /Q Tile\\terrain\\desert\\*.anim
del /Q Tile\\terrain\\ground\\*.anim
del /Q Tile\\terrain\\ice\\*.anim
del /Q Tile\\terrain\\snow\\*.anim
del /Q Tile\\other\\*.anim
del /Q player\\luigi\\*.anim
del /Q enemy\\boo\\*.anim
del /Q enemy\\chain_chomp\\*.anim
del /Q enemy\\koompa\\*.anim
del /Q enemy\\piranha_plant\\*.anim
del /Q enemy\\tartosso\\*.anim

ECHO "Eliminazione completata con successo"

pause