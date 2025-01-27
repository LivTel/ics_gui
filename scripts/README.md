# Instrument Control System Graphical User Interface scripts

This directory contains some scripts, mainly related to deployment of the software.

Usually the IcsGUI is deployed as part of the instrument deployment tarball.

## IcsGUI standaalone deployment.

It can also be deployed as a standa-alone system on operations machines, See the [README](README) and [README.DEPLOYMENT](README.DEPLOYMENT) files.

Note, all logs are deleted during installation. Any configuration changes from the default will
also be lost as all properties file in the /tmc/bin tree are overwritten with the original version.
To preserve changes, copy the properties files (*.properties) somewhere safe before installation.
Better yet, update the originals on the ARI development account.

### To create an installation

1. **cd ~dev/src/ics_gui/scripts**
2. **./icsgui_make_deployment <machine name>**
3. Wait for "Deployment ready." message.

### To deploy the deployment

1. Put the following files in /home/eng/tmp/&lt;date&gt;:
  * http://ltdevsrv.livjm.ac.uk/~dev/ics_gui/deployment/icsgui_tar_install
  * http://ltdevsrv.livjm.ac.uk/~dev/ics_gui/deployment/tmc_cshrc
  * http://ltdevsrv.livjm.ac.uk/~dev/ics_gui/deployment/tmc_cshrc_edit.awk
  * http://ltdevsrv.livjm.ac.uk/~dev/ics_gui/deployment/icsgui_deployment_&lt;machine name&gt;.tar.gz
2. Ensure icsgui_tar_install is executable:
	**chmod +x icsgui_tar_install**
3. **su** (become root).
4. **./icsgui_tar_install &lt;machine name&gt;**

## IcsGUI docker deployment.

The IcsGUI can also be deployed as a docker container.

See the [images](../images) directory for details of how to create the docker container.

The config files for the ics_gui have to be deployed from a tarball, created by the **ics_gui_create_config_tarball** script in this directory. i.e.

* **cd ~dev/src/ics_gui/scripts/**
* **./ics_gui_create_config_tarball &lt;instrument machine&gt;**

See the images [README](../images/README.md) for details of how to use the script and deploy the resulting tarball.
