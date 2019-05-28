import hudson.tasks.test.AbstractTestResultAction
import hudson.tasks.junit.CaseResult

def getTestSummary = { ->
    def testResultAction = currentBuild.rawBuild.getAction(AbstractTestResultAction.class)
    def summary = ""

    if (testResultAction != null) {
        total = testResultAction.getTotalCount()
        failed = testResultAction.getFailCount()
        skipped = testResultAction.getSkipCount()

        summary = "Passed: " + (total - failed - skipped)
        summary = summary + (", Failed: " + failed)
        summary = summary + (", Skipped: " + skipped)
    } else {
        summary = "No tests found"
    }
    return summary
}


node {
	stage('Payment Testsuite Execution'){
		checkout([$class: 'GitSCM',
		branches: [[name: '*/master']],
		userRemoteConfigs: [
		[url: 'https://github.com/SujataKale97/test.git',
		credentialsId: 'ea4c3770-b2ed-4639-9ffc-cc3e586e454c']
		]])  
			
                 bat ' gradlew ' +
                      
                 "clean test -Ppayment "
	
		publishHTML([reportDir: 'test-output', reportFiles: 'PaymentServiceReport.html', reportName: 'Payment Test-suite Report'])
		step $class: 'JUnitResultArchiver', testResults: 'test-output/testng-results.xml'
		testSummary = getTestSummary()
		println(testSummary)
	currentBuild.result = 'UNSTABLE'
	}
	/*stage('Email')
        {
            env.ForEmailPlugin = env.WORKSPACE      
            emailext ( attachmentsPattern: "test-output/PaymentServiceReport.html", 
            body: " ${currentBuild.currentResult}: Job ${env.JOB_NAME} build ${env.BUILD_NUMBER}\n More info at: ${env.BUILD_URL}", 
            subject: "Jenkins Build ${currentBuild.currentResult}: Job ${env.JOB_NAME}",
	    to: "kale.babanrao@happiestminds.com"
            
	     )
		
        }*/
/*	stage('SonarQube analysis') {
   // ws('C:\\Apps\\Jenkins\\jobs\\trial') {
    // requires SonarQube Scanner 2.8+
    def scannerHome = tool 'sonarScanner';
    withSonarQubeEnv('sonar') {
	   bat "gradlew -Dsonar.analysis.mode "
      bat "${scannerHome}/StartSonar.bat"
    }
  
}*/
}
