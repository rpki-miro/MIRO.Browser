# MIRO.Browser
Web application that allows browsing and inspection of RPKI objects

##API
The MIRO API allows you to filter for specific groups of objects and link to them or download them
in JSON format.

###Browser
Download or view RPKI objects

**Context path: /browser**

Parameter | Value
---------|---------
trustAnchor | Trust anchor names, as seen in the dropdown menu of the RPKI Browser (e.g ARIN, RIPE, AFRINIC). **This parameter needs to be present.**
validationStatus | "passed", "error", or "warning" or any comma seperated combination of those 3 strings
filetype | "roa", "cer", or "all"
attributeKey | Can be "filename", "location", "subject", "issuer", "serial_nr", "resource". See also the graphical filter widget in MIRO.
attributeValue | Corresponding value to 'attributeKey'. *Example: attributeKey=resource&attributeValue=102.45.0.0/16*
dl | If this is present, the request will be answered with a json file of the specified objects. If it is not present, the request will be answered with the preset RPKI Browser GUI.

#### Examples
Download the complete RIPE certificate tree:

http://rpki-browser.realmv6.org/browser?trustAnchor=RIPE&dl=true

Download all ROAs in the LACNIC certificate tree with resource 'AS11562':

http://rpki-browser.realmv6.org/browser?trustAnchor=LACNIC&attributeKey=resource&attributeValue=AS11562&filetype=roa&dl=true

###Statistics
Download or view basic statistics about the RPKI

**Context path: /stats**

Parameter | Value 
----------|----------
trustAnchor | Trust anchor names, as seen in the tabs of the Statistics widget in MIRO (e.g ARIN, RIPE, AFRINIC). **This parameter needs to be present.**
dl | If this is present, the request will be answered with a json file containing the specified stats. If it is not present, the request will be answered with the preset Statistics GUI.

#### Examples
Download statistics about the ARIN certificate tree:

http://rpki-browser.realmv6.org/stats?trustAnchor=ARIN&dl=true

