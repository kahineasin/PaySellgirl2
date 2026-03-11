��װ��������Ŀ��ʹ��:
mvn install:install-file -DgroupId=com.sellgirl -DartifactId=sellgirlPayHelper -Dversion=0.0.1-SNAPSHOT -Dfile=sellgirlPayHelper-0.0.1-SNAPSHOT.jar -Dpackaging=jar -DgeneratePom=true
mvn install:install-file -DgroupId=pf.java -DartifactId=pfHelper -Dversion=0.0.1-SNAPSHOT -Dfile=pfHelper-0.0.1-SNAPSHOT.jar -Dpackaging=jar -DgeneratePom=true

win10上要在git-bash上执行（否则不会自动更新maven-plugins导致报错）
mvn install:install-file -Dfile=sqljdbc42.jar -Dpackaging=jar -DgroupId=com.microsoft.sqlserver -DartifactId=sqljdbc4 -Dversion=6.0

逐步把此项目的非spring类移动到pfHelperNotSpring