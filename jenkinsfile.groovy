node{
stage('Payment Testsuite Execution'){


			checkout([$class: 'GitSCM',
			  branches: [[name: '*/master']],
			  userRemoteConfigs: [
				[url: 'https://github.com/SujataKale97/new1.git',
				 credentialsId: 'ea4c3770-b2ed-4639-9ffc-cc3e586e454c']
			  ]
			])  
			
                        bat ' ./gradlew ' +
                       "clean build " +
                       "test -Ppayment"
			 publishHTML([reportDir: 'test-output', reportFiles: 'PaymentServiceReport.html', reportName: 'Payment Test-suite Report'])
		  }
      }
