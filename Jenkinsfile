node {
  stage('SCM') {
    checkout scm
  }
  stage('SonarQube Analysis') {
    def mvn = tool 'maven';
    withSonarQubeEnv() {
      sh "${mvn}/bin/mvn clean verify sonar:sonar -Dsonar.projectKey=IssamLaoumri_social-media-app-backend_ae422c33-fcf3-44bf-be8d-ef064621582e -Dsonar.projectName='social-media-app-backend'"
    }
  }
}