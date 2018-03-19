
title first batch program

ECHO "Script per la cancellazione deglle animazioni"

del /s /Q %1\\tile\\*.anim
del /s /Q %1\\player\\*.anim
del /s /Q %1\\enemy\\*.anim

ECHO "Eliminazione completata con successo"

pause