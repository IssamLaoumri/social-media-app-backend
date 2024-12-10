node {
  stage('SCM') {
    checkout scm
  }
  stage('SonarQube Analysis') {
    def mvn = tool 'maven';
    def sonarHome = tool 'sonarqube';
    withSonarQubeEnv(installationName: 'SonarQube') {
      sh "${mvn}/bin/mvn clean ${sonarHome}/bin/sonar-scanner -Dsonar.projectKey=IssamLaoumri_social-media-app-backend_ae422c33-fcf3-44bf-be8d-ef064621582e -Dsonar.projectName='social-media-app-backend'"
    }
  }
}
//mvn clean verify sonar:sonar   -Dsonar.projectKey=social_media_backend   -Dsonar.projectName='social_media_backend'   -Dsonar.host.url=http://localhost:9000    -Dsonar.token=sqp_4550881832e655d7540935139bf803067396e134