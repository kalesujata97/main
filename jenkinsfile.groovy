node{
stage('Payment Testsuite Execution'){


			checkout([$class: 'GitSCM',
			  branches: [[name: '*/master']],
			  userRemoteConfigs: [
				[url: 'https://github.com/SujataKale97/new.git',
				 credentialsId: 'ea4c3770-b2ed-4639-9ffc-cc3e586e454c']
			  ]
			])  
			 bat 'gradle clean build'
       bat 'gradle test -Ppayment '
       
			 publishHTML([reportDir: 'test-output', reportFiles: 'PaymentServiceReport.html', reportName: 'Payment Test-suite Report'])
		  }
      }
