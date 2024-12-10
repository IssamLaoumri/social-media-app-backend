node {
  stage('SCM') {
    checkout scm
  }
  stage('SonarQube Analysis') {
    def mvn = tool 'maven';
    withSonarQubeEnv('sonarqube') {
      bat "${mvn}/bin/mvn clean verify sonar:sonar -Dsonar.projectKey=social_media_backend_pipepline -Dsonar.projectName='social_media_backend_pipepline'"
    }
  }
}
//