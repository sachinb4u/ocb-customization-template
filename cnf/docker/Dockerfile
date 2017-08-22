FROM openjdk:8-jre

# Define home directory
ENV OCB_HOME /ocb

# set working directory
WORKDIR $OCB_HOME

# Copy configurations folders e.g. config.local, config.qa1, config.qa2
COPY . $OCB_HOME/

# Copy executable jar
COPY *.jar $OCB_HOME/

# Expected folder structure would be as shown below
# ********************************************************************
# ocb/
#	- keyfile.properties
#	- workflow.jar
#	+ config.local
#		- com.sap.banking.common_encryption.cfg
#		- com.sap.banking.ocbconfig.cfg
#		- com.sap.banking.persistence.cfg
#	+ config.qa1
#		- com.sap.banking.common_encryption.cfg
#		- com.sap.banking.ocbconfig.cfg
#		- com.sap.banking.persistence.cfg
#	+ config.qa2
#		- com.sap.banking.common_encryption.cfg
#		- com.sap.banking.ocbconfig.cfg
#		- com.sap.banking.persistence.cfg
# ********************************************************************

# Expose HTTP port
EXPOSE 8888

# Run Service
CMD java -jar -Dfelix.fileinstall.dir=$OCB_HOME/config.${config:-local} $(ls $OCB_HOME/*.jar)

# Run the container with below command
# docker run -p 8989:8080 --add-host="localhost:10.136.67.247" workflow

# QA Run
# docker run -p 7171:8080 --add-host="localhost:10.136.67.247" --env db=qa-mssql ocb/workflow:1.0.1
