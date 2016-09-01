# MIRO.Browser
Web application that allows browsing and inspection of RPKI objects

## Install
There is a publicly available instance of MIRO running at http://rpki-browser.realmv6.org/

If you wish to deploy MIRO yourself, download the latest release and follow these instruction:

### Web archive file
The release contains the file 'miro.war'. This needs to be deployed with a servlet container such as [http://tomcat.apache.org/]tomcat.

In case of tomcat you can just drop the miro.war file into tomcat/webapps.

### Necessary directory structure
In order for MIRO to work, you need to copy the directory "MIRO" found in the release archive to "/var/data". 
IMPORTANT: The user who is running the servlet container needs to have read/write access to /var/data/MIRO

### Adding your own trust anchor locators
The default directory for trust anchor locators is /var/data/MIRO/Browser/tals/
The .tal files are grouped in sub-directories (RIPE, ARIN, APNIC, LACNIC, AFRINIC).
If you wish to add your own trust anchor locators, make a new sub-directory and place your .tals in there:
/var/data/MIRO/Browser/tals/<your-sub-directory>/<your-tal>.tal

Trust anchor locators are grouped by the repository the trust anchor resides is, so APNICs 5 trust anchors are all grouped together. This is done to make prefetching easier.

#### Prefetching
Some repositories have a flat structure, and thus the normal recursive fetching process takes a long time (using rsync). To shorten the process prefetching aims to download as much of the repository in advance. In MIRO/Browser/prefetching you can see the URIs set to being prefetched for every TAL grouping.

### Updating
In order to trigger an update, a connection coming from the loopback interface needs to be made to the update port (default 9234).
One way to do this (python):
```
sock = socket.socket()
try:
	sock.connect(("localhost",9234))
	sock.close()
except socket_error as e:
	print(e)
 ```
 To setup regular updates, use a cronjob.

### Export
The last downloaded resource certificate trees as well as ROAs are saved at /var/data/MIRO/Browser/exports

### Config file
In the conf file /var/data/MIRO/Browser/miro.browser.conf you can change the update port (default 9234)


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

