import jenkins.model.*
import hudson.model.*
import org.jenkinsci.plugins.*
import hudson.model.UpdateSite
import hudson.PluginWrapper

Set<String> plugins_to_install = [
    // "git",
    // "docker",
    "locale",
    "performance"
]

Boolean hasConfigBeenUpdated = false
UpdateSite updateSite = Jenkins.getInstance().getUpdateCenter().getById('default')
List<PluginWrapper> plugins = Jenkins.instance.pluginManager.getPlugins()

def install_plugin(shortName, UpdateSite updateSite) {
    println "Installing ${shortName} plugin."
    UpdateSite.Plugin plugin = updateSite.getPlugin(shortName)
    Throwable error = plugin.deploy(false).get().getError()
    if(error != null) {
        println "ERROR installing ${shortName}, ${error}"
    }
    null
}

// Check the update site(s) for latest plugins
println 'Checking plugin updates via Plugin Manager.'
Jenkins.instance.pluginManager.doCheckUpdatesServer()

// Any plugins need updating?
Set<String> plugins_to_update = []
plugins.each {
    if(it.hasUpdate()) {
        plugins_to_update << it.getShortName()
    }
}

if(plugins_to_update.size() > 0) {
    println "Updating plugins..."
    plugins_to_update.each {
        install_plugin(it, updateSite)
    }
    println "Done updating plugins."
    hasConfigBeenUpdated = true
}

// Get a list of installed plugins
Set<String> installed_plugins = []
plugins.each {
    installed_plugins << it.getShortName()
}

// Check to see if there are missing plugins to install
Set<String> missing_plugins = plugins_to_install - installed_plugins
if(missing_plugins.size() > 0) {
    println "Install missing plugins..."
    missing_plugins.each {
        install_plugin(it, updateSite)
    }
    println "Done installing missing plugins."
    hasConfigBeenUpdated = true
}

if(hasConfigBeenUpdated) {
    println "Saving Jenkins configuration to disk."
    Jenkins.instance.save()
    Jenkins.instance.restart()
} else {
    println "Jenkins up-to-date. Nothing to do."
}