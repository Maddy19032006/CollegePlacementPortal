pipeline {
    agent any

    tools {
        // Configure these JDK/Maven installations in Jenkins (Manage Jenkins > Tools)
        jdk 'jdk25'
        maven 'maven3'
    }

    environment {
        // SonarQube: create a "sonar-token" Secret Text credential in Jenkins.
        // The SonarQube server URL is configured in Manage Jenkins > System.
        SCANNER_HOME = tool 'sonar-scanner'
    }

    options {
        timestamps()
        disableConcurrentBuilds()
        buildDiscarder(logRotator(numToKeepStr: '20'))
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                bat 'mvn -B clean compile'
            }
        }

        stage('Unit & Integration Tests') {
            steps {
                bat 'mvn -B test'
            }
            post {
                always {
                    junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml'
                }
            }
        }

        stage('Code Quality (SonarQube)') {
            when {
                // Runs only when SonarQube is configured; see docs/setup-and-verification.md
                expression { env.SONAR_HOST_URL != null }
            }
            steps {
                withCredentials([string(credentialsId: 'sonar-token', variable: 'SONAR_TOKEN')]) {
                    bat 'mvn -B sonar:sonar -Dsonar.host.url=%SONAR_HOST_URL% -Dsonar.token=%SONAR_TOKEN%'
                }
            }
        }

        stage('Package') {
            steps {
                bat 'mvn -B -DskipTests package'
            }
            post {
                success {
                    archiveArtifacts artifacts: 'target/placement-portal.jar', fingerprint: true
                }
            }
        }

        stage('Docker Build') {
            when { expression { env.DOCKER_REGISTRY != null } }
            steps {
                withCredentials([usernamePassword(
                        credentialsId: 'docker-registry',
                        usernameVariable: 'DOCKER_USER',
                        passwordVariable: 'DOCKER_PASS')]) {
                    bat 'docker build -t %DOCKER_REGISTRY%/placement-portal:%BUILD_NUMBER% .'
                }
            }
        }

        stage('Docker Push') {
            when { expression { env.DOCKER_REGISTRY != null } }
            steps {
                withCredentials([usernamePassword(
                        credentialsId: 'docker-registry',
                        usernameVariable: 'DOCKER_USER',
                        passwordVariable: 'DOCKER_PASS')]) {
                    bat 'docker login -u %DOCKER_USER% -p %DOCKER_PASS% %DOCKER_REGISTRY%'
                    bat 'docker push %DOCKER_REGISTRY%/placement-portal:%BUILD_NUMBER%'
                }
            }
        }

        stage('Deploy') {
            when { expression { env.DEPLOY_TARGET != null } }
            steps {
                // DEPLOY_TARGET example: "user@server:/opt/placement-portal"
                // This copies the compose project and restarts the containers.
                bat 'docker compose -f docker-compose.yml up -d --build'
            }
        }

        stage('Smoke Test') {
            steps {
                // Waits for the app to answer on the login page; fails the build otherwise.
                bat '''
                    timeout 90
                    for /L %%i in (1,1,30) do (
                        curl -s -o NUL -w "%%{http_code}" http://localhost:8081/login.html | findstr "200" && exit /b 0
                        timeout /t 3 /nobreak >NUL
                    )
                    exit /b 1
                '''
            }
        }
    }

    post {
        success {
            echo 'Pipeline completed successfully.'
        }
        failure {
            echo 'Pipeline failed. Check the stage that failed first.'
        }
        always {
            cleanWs()
        }
    }
}
