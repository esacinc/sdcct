package gov.hhs.onc.sdcct.tools.codegen

import com.sun.tools.xjc.Plugin

interface CodegenPlugin {
    com.sun.tools.ws.wscompile.Plugin forWsimport()

    Plugin forXjc()

    String getOptionName()

    String getUsage()
}