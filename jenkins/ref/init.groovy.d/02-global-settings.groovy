import jenkins.model.*
import org.jenkinsci.plugins.*
import hudson.security.csrf.DefaultCrumbIssuer
import hudson.plugins.locale.PluginImpl

def instance = Jenkins.getInstance()

println("--- Configuring global getting")
instance.setNumExecutors(1)
instance.setCrumbIssuer(new DefaultCrumbIssuer(true))
instance.setNoUsageStatistics(true)
instance.save()

println("--- Configuring locale")
PluginImpl localePlugin = (PluginImpl)instance.getPlugin("locale")
localePlugin.systemLocale = "en_US"
localePlugin.@ignoreAcceptLanguage=true

// println("--- Configuring git global options")
// def desc = instance.getDescriptor("hudson.plugins.git.GitSCM")
// desc.setGlobalConfigName("jenkins")
// desc.setGlobalConfigEmail("jenkins@example.com")
// desc.save()

println("--- Configuring protocols")
Set<String> agentProtocolsList = ['JNLP4-connect', 'Ping']
if(!instance.getAgentProtocols().equals(agentProtocolsList)) {
    instance.setAgentProtocols(agentProtocolsList)
    println "Agent Protocols have changed. Setting: ${agentProtocolsList}"
    instance.save()
}
else {
    println "Nothing changed. Agent Protocols already configured: ${instance.getAgentProtocols()}"
}