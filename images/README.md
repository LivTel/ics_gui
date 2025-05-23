# IcsGUI : images

This directory contains a Dockerfile for building an IcsGUI docker container.

## Deployment

### IcsGUI docker container

To build a docker container do the following (on an LT development machine, where the ics_gui software repository is installed at /home/dev/src/ics_gui) :

* **cd ~dev/src/ics_gui/images** (i.e. this directory)
* **./provision_ics_gui** Run the provisioning script, which copies the Java libraries from /home/dev/bin/javalib, the IcsGUI class files and wavs/icons from /home/dev/bin/ics_gui/java/ and sub-directories, into a created **docker** directory tree (created in the images directory). This allows us to use a local context for the docker build.
* **docker build -f ics_gui -t ics_gui_image .** Build the docker container from the **ics_gui** file.
* **docker save -o ics_gui_image.tar ics_gui_image** Save the constructed docker container into the **ics_gui_image.tar** tarball.

This saved docker tarball can then be copied to the instrument control computer as follows:

* **scp -C ics_gui_image.tar admin@&lt;instrument machine&gt;:images**

The docker can then be installed / loaded into the local system as follows:

* **ssh admin@&lt;instrument machine&gt;**
* **cd images**
* **docker load -i ics_gui_image.tar**

You now need to install the IcsGUI config files before starting the docker.

### IcsGUI config files

To generate a tarball containing the IcsGUI config files, on an LT development machine , where the ics_gui software repository is installed at /home/dev/src/ics_gui, do the following:

* **cd ~dev/src/ics_gui/scripts/**
* **./ics_gui_create_config_tarball &lt;instrument machine&gt;** 

The config tarball ends up at: ~dev/public_html/ics_gui/deployment/ics_gui_&lt;instrument machine&gt;_config_deployment.tar.gz . Copy the config tarball to the instrument machine:

* **scp -C ~dev/public_html/ics_gui/deployment/ics_gui_&lt;instrument machine&gt;_config_deployment.tar.gz admin@&lt;instrument machine&gt;:images**

Then install the config tarball as follows:

* **ssh admin@&lt;instrument machine&gt;**
* **cd /**
* **sudo tar xvfz /home/admin/images/ics_gui_&lt;instrument machine&gt;_config_deployment.tar.gz** 
* **sudo chown root:root /** This fixes root's permissions.

### Starting the IcsGUI as a docker

The IcsGUI can then be started as follows:

* **docker run -e DISPLAY=$DISPLAY -p 7383:7383 --mount type=bind,src=/icc,dst=/icc --mount type=bind,src=/tmp,dst=/tmp -it -d ics_gui_image**

For this to work the IcsGUI config files need to have been installed under **/icc** first. 

An explanation of the command line:
* **-e DISPLAY=$DISPLAY** : Set the DISPLAY environment variable to where you want the GUI to appear. Note you should specify DISPLAY as an IP address rather than a resolvable name, the DISPLAY is opened from inside the docker, which has no entries in /etc/hosts (i.e. use **export DISPLAY=192.168.1.30:1** , NOT **export DISPLAY=occ:1**)
* **-p 7383:7383** : allow access to server port 7383 (fake ISS server port)
* **--mount type=bind,src=/icc,dst=/icc** : allow docker to access /icc as /icc to load the IcsGUI config files, and write logs
* **--mount type=bind,src=/tmp,dst=/tmp** : allow docker to access /tmp as /tmp.  The IcsGUI is a X Swing application, and needs to access the host machines X server to display it's windows. This allows us to access  **/tmp/.X11-unix/Xn**, where 'n' is the selected X display, which is where modern Linux's / Xorg's put there Unix sockets for contacting the X display. You don't need this part of the command line if using IcsGUI via a network port, or via a VNC server (which uses a network port).
* **-d** : docker is a daemon (detach from terminal)
* **-it** : -t allocate a pseodo-tty, -i interactive. Do we need these?

### Stopping the IcsGUI docker

Just quit the GUI (File-&gt;Exit).

The IcsGUI can be stopped from the command line as follows:

* **docker ps**
Find the **ics-gui** container id and then do the following:

* **docker kill &lt;containerid&gt;**
* **docker remove &lt;containerid&gt;**

You need to remove the container to re-use the ics-gui container name.
