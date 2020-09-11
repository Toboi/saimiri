pipeline {
	agent none
	stages {
		stage('Build on openJDK 8') {
			agent {
				docker { image 'openjdk:8-alpine' }
			}
			steps {
				sh './gradlew build'
			}
		}
	}
}
