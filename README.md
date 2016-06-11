1. Add your key
ssh-add your-key-path

2. Send the jar to cluster
rsync -avz /path/wukong-assembly-0.1.jar username@ip:/path

3. Submit Job to Yarn
/opt/spark-1.5/bin/spark-submit --class com.alvin.wukong.apps.ETLApp
--master yarn-cluster --num-executors 8  --executor-memory 12g --driver-memory 4g --executor-cores 8
--driver-java-options "-XX:MaxPermSize=2048m -Dconfig.resource=/dev.conf "
--files /path/ETLApp.conf /path/wukong-assembly-0.1.jar
--env dev --configFile ETLApp.conf