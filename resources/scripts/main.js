// it is really useful for development
var mod = Vars.mods.getMod("scheme-size");

// V8 FIX: In Mindustry v8, mods share a classloader, so mod.loader is null.
// We must use Vars.mods.mainLoader() instead to grab the Java classes.
var get = (pkg) => Vars.mods.mainLoader().loadClass(pkg).newInstance();

// classloaders are restricted on mobile devices, so keep this check
if (Vars.mobile) get = (pkg) => null;

const SchemeMain = mod.main;
const SchemeVars = get("scheme.SchemeVars");
const SchemeUpdater = get("scheme.SchemeUpdater");
const Backdoor = get("scheme.Backdoor");
const ServerIntegration = get("scheme.ServerIntegration");
const ClajIntegration = get("scheme.ClajIntegration");
const ModedSchematics = get("scheme.moded.ModedSchematics");