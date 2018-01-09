@ECHO OFF
title first batch program

ECHO "Script per la cancellazione deglle animazioni"

del /s /Q tile\\*.ti
del /s /Q player\\*.ac
del /s /Q enemy\\*.ac

ECHO "Eliminazione completata con successo"

pause