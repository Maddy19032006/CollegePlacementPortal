pipeline {
    agent any

    tools {
        // Uses the configured Maven 'M3' tool in Jenkins
        maven 'M3'
    }

    environment {
        // ── Docker Hub ──────────────────────────────────────────────────────────
        // Uses existing 'docker-hub-credentials' in Jenkins
        DOCKER_HUB_CREDS = credentials('docker-hub-credentials')
        IMAGE_NAME = "theatomicxm/placement-portal"

        // ── GitHub ───────────────────────────────────────────────────────────────
        // Repo: https://github.com/Maddy19032006/CollegePlacementPortal
    }

    options {
        timestamps()
        disableConcurrentBuilds()
        buildDiscarder(logRotator(numToKeepStr: '10'))
    }

    stages {

        // ── Stage 1 ─────────────────────────────────────────────────────────────
        stage('Checkout from GitHub') {
            steps {
                checkout scm
                echo "Checked out branch: ${env.GIT_BRANCH} @ ${env.GIT_COMMIT}"
            }
        }

        // ── Stage 2 ─────────────────────────────────────────────────────────────
        stage('Build & Compile') {
            steps {
                bat 'mvn -B clean compile -q'
            }
        }

        // ── Stage 3 ─────────────────────────────────────────────────────────────
        stage('Automated Tests (JUnit + JaCoCo)') {
            steps {
                bat 'mvn -B test jacoco:report'
            }
            post {
                always {
                    junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml'
                    echo 'Test results published. Check the Test Results trend in the job sidebar.'
                }
            }
        }

        // ── Stage 4 ─────────────────────────────────────────────────────────────
        stage('Package JAR') {
            steps {
                bat 'mvn -B -DskipTests package -q'
            }
            post {
                success {
                    archiveArtifacts artifacts: 'target/placement-portal*.jar', fingerprint: true
                    echo 'Executable JAR archived as a Jenkins build artifact.'
                }
            }
        }

        // ── Stage 5 ─────────────────────────────────────────────────────────────
        stage('Docker Build Image') {
            steps {
                bat "docker build -t %IMAGE_NAME%:latest -t %IMAGE_NAME%:%BUILD_NUMBER% ."
                echo "Docker image built: ${env.IMAGE_NAME}:${env.BUILD_NUMBER}"
            }
        }

        // ── Stage 6 ─────────────────────────────────────────────────────────────
        stage('Docker Push to Hub') {
            steps {
                bat "docker login -u %DOCKER_HUB_CREDS_USR% -p %DOCKER_HUB_CREDS_PSW%"
                bat "docker push %IMAGE_NAME%:latest"
                bat "docker push %IMAGE_NAME%:%BUILD_NUMBER%"
                echo "Image pushed to Docker Hub: https://hub.docker.com/r/${env.IMAGE_NAME}"
            }
        }

        // ── Stage 7 ─────────────────────────────────────────────────────────────
        stage('Deploy with Docker Compose') {
            steps {
                bat "set APP_IMAGE=%IMAGE_NAME%:%BUILD_NUMBER% && docker compose -f docker-compose.yml up -d --pull always --no-build"
                echo 'Containers restarted with new image.'
            }
        }

        // ── Stage 8 ─────────────────────────────────────────────────────────────
        stage('Smoke Test') {
            steps {
                bat '''
                    echo Waiting for application to become healthy...
                    for /L %%i in (1,1,30) do (
                        curl -sf -o NUL -w "%%{http_code}" http://localhost:8081/login.html | findstr /C:"200" && (
                            echo App is UP and responding.
                            exit /b 0
                        )
                        timeout /t 3 /nobreak >NUL
                    )
                    echo ERROR: App did not respond within 90 seconds.
                    exit /b 1
                '''
            }
        }
    }

    post {
        success {
            echo "=== PIPELINE PASSED === Build #${env.BUILD_NUMBER} deployed successfully."
            echo "App URL: http://localhost:8081"
            echo "Docker Hub: https://hub.docker.com/r/${env.IMAGE_NAME}"
        }
        failure {
            echo "=== PIPELINE FAILED === Check the stage that failed above for details."
        }
        always {
            bat 'docker image prune -f'
            cleanWs()
        }
    }
}
