pipeline {
    agent any

    environment {
        // ─── Dynamic Configuration ───────────────────────────────
        DOCKERHUB_USERNAME     = 'abdulrazzakjakati'
        APP_NAME               = 'food-delivery-restaurant-service'
        GITOPS_REPO_URL        = 'git@github.com:abdulrazzakjakati/deployment-folder.git'
        GITOPS_BRANCH          = 'master'
//        MANIFEST_PATH          = 'aws/restaurant-manifest.yml'
        MANIFEST_PATH          = 'helm/restaurant-service/values.yaml'
        SONAR_PROJECT_KEY      = 'com.codeddecode:restaurantlisting'
        SONAR_URL              = 'http://140.245.14.252:9000'
        COVERAGE_THRESHOLD     = '50.0'
        // ─────────────────────────────────────────────────────────

        DOCKERHUB_CREDENTIALS  = credentials('DOCKER_HUB_CREDENTIAL')
        SONAR_TOKEN            = credentials('sonar-token')
        VERSION                = "${env.BUILD_ID}"
        DOCKER_IMAGE           = "${DOCKERHUB_USERNAME}/${APP_NAME}"
    }

    tools {
        maven 'Maven'
    }

    stages {
        stage('Maven Build') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Run Tests') {
            steps {
                sh 'mvn test'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                sh """
                    mvn clean verify sonar:sonar \
                    -Dsonar.host.url=${SONAR_URL} \
                    -Dsonar.token=${SONAR_TOKEN} \
                    -Dsonar.projectKey=${SONAR_PROJECT_KEY}
                """
            }
        }

        stage('Check Code Coverage') {
            steps {
                script {
                    withCredentials([string(credentialsId: 'sonar-token', variable: 'SONAR_TOKEN')]) {
                        // Call SonarQube API securely
                        def response = sh(
                                script: """curl -s -u "$SONAR_TOKEN:" "${SONAR_URL}/api/measures/component?component=${SONAR_PROJECT_KEY}&metricKeys=coverage" """,
                                returnStdout: true
                        ).trim()

                        echo "SonarQube API response: ${response}"

                        // Parse JSON in Groovy
                        def json = new groovy.json.JsonSlurper().parseText(response)
                        def coverageStr = json?.component?.measures?.getAt(0)?.value ?: "0"

                        echo "Coverage raw value: ${coverageStr}%"

                        def coverage = coverageStr.toDouble()

                        if (coverage < COVERAGE_THRESHOLD.toDouble()) {
                            error "Coverage ${coverage}% < ${COVERAGE_THRESHOLD}% threshold. Fix tests!"
                        }
                    }
                }
            }
        }

        stage('Docker Build & Push') {
            steps {
                sh 'echo $DOCKERHUB_CREDENTIALS_PSW | docker login -u $DOCKERHUB_CREDENTIALS_USR --password-stdin'
                sh "docker build -t ${DOCKER_IMAGE}:${VERSION} ."
                sh "docker push ${DOCKER_IMAGE}:${VERSION}"
            }
        }

        stage('Update GitOps Manifests') {
            steps {
                checkout scmGit(
                    branches: [[name: "*/${GITOPS_BRANCH}"]],
                    userRemoteConfigs: [[credentialsId: 'git-ssh', url: "${GITOPS_REPO_URL}"]]
                )
                script {
                    sh """
                        sed -i "s|image:.*|image: ${DOCKER_IMAGE}:${VERSION}|" ${MANIFEST_PATH}
                    """
                    sh 'git add .'
                    sh "git commit -m 'Update ${APP_NAME} to v${VERSION}' || true"
                    sshagent(['git-ssh']) {
                        sh "git push origin ${GITOPS_BRANCH}"
                    }
                }
            }
        }

        stage('Cleanup') {
            steps {
                deleteDir()
            }
        }
    }

    post {
        always {
            cleanWs()
        }
    }
}