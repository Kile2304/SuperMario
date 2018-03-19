
title first batch program

ECHO "Script per la cancellazione deglle animazioni"

del /s /Q %cd%\\Animazioni\\tile\\*.anim
del /s /Q %cd%\\Animazioni\\player\\*.anim
del /s /Q %cd%\\Animazioni\\enemy\\*.anim

ECHO "Eliminazione completata con successo"

pause