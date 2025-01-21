# IcsGUI : images

This directory contains a Dockerfile for building an IcsGUI docker container.

## Deployment

### IcsGUI docker container

To build a docker container do the following (on an LT development machine, where the ics_gui software repository is installed at /home/dev/src/ics_gui) :

* **cd ~dev/src/ics_gui/images** (i.e. this directory)
* **sudo docker build -f ics_gui -t ics_gui_image /** Build the docker container from the **ics_gui** file.
* **docker save -o ics_gui_image.tar ics_gui_image** Save the constructed docker container into the **ics_gui_image.tar** tarball.

This saved docker tarball can then be copied to the instrument control computer as follows:

* **scp -C ics_gui_image.tar admin@&lt;instrument machine&gt;:images**

The docker can then be installed / loaded into the local system as follows:

* **ssh admin@&lt;instrument machine&gt;**
* **cd images**
* **sudo docker load -i ics_gui_image.tar**

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

* **sudo docker run -e DISPLAY=$DISPLAY -p 7383:7383 --mount type=bind,src=/icc,dst=/icc --name=ics-gui -it -d ics_gui_image**

or

* **sudo docker run --net=host -e DISPLAY=$DISPLAY --mount type=bind,src=/icc,dst=/icc --name=ics-gui -it -d ics_gui_image**

For this to work the IcsGUI config files need to have been installed under **/icc** first. 

An explanation of the command line:
* **-e DISPLAY=$DISPLAY** : Set the DISPLAY environment variable to where you want the GUI to appear
* **-p 7383:7383** : allow access to server port 7383 (fake ISS server port)
* **--mount type=bind,src=/icc,dst=/icc** : allow docker to access /icc as /icc to load the IcsGUI config files, and write logs
* **--name=ics-gu** docker is called ics-gu in docker ps
* **-d** : docker is a daemon (detach from terminal)
* **-it** : -t allocate a pseodo-tty, -i interactive. Do we need these?

### Stopping the IcsGUI docker

Just quit the GUI (File-&gt;Exit).

The IcsGUI can be stopped from the command line as follows:

* **sudo docker ps**
Find the **ics-gui** container id and then do the following:

* **sudo docker kill &lt;containerid&gt;**
* **sudo docker remove &lt;containerid&gt;**

You need to remove the container to re-use the ics-gui container name.
