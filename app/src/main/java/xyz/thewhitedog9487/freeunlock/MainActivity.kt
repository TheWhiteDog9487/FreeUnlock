package xyz.thewhitedog9487.freeunlock

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.service.quicksettings.TileService
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.core.graphics.drawable.toBitmap
import com.highcapable.yukihookapi.YukiHook
import com.highcapable.yukihookapi.hook.factory.systemDataChannel
import com.highcapable.yukihookapi.hook.log.data.YLogData
import com.highcapable.yukihookapi.hook.xposed.application.ModuleApplication
import com.highcapable.yukihookapi.hook.xposed.scope.YukiHookModuleScope
import xyz.thewhitedog9487.freeunlock.UI.Composable.LinkRow
import xyz.thewhitedog9487.freeunlock.UI.Composable.StatusRow
import xyz.thewhitedog9487.freeunlock.UI.Icon.Icons
import xyz.thewhitedog9487.freeunlock.UI.Theme.FreeUnlockTheme
import xyz.thewhitedog9487.freeunlock.Xposed.DataChannelKeys
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.onFailure

class FreeUnlockApplication: ModuleApplication()
class MainActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MainUI() } } }

enum class MainTabs(
    @StringRes val Label: Int,
    val Icon: ImageVector ) {
    Home(R.string.NavigationBarIcon_Home, Icons.Home),
    LogHistory(R.string.NavigationBarIcon_History, Icons.History),
    About(R.string.NavigationBarIcon_About, Icons.Info) }

class ModuleStatusState {
    var IsModuleActive by mutableStateOf(false)
    var IsChecking by mutableStateOf(true)
    var IsHookConnected by mutableStateOf<Boolean?>(null)
    var IsEnabled by mutableStateOf(true)

    var FrameworkName by mutableStateOf("-")
    var FrameworkVersion by mutableStateOf("-")
    var ApiVersion by mutableStateOf("-")
    var RuntimeName by mutableStateOf("-")
    var XposedScope by mutableStateOf<YukiHookModuleScope?>(null)
    private val MainHandler = Handler(Looper.getMainLooper())
    private val MarkHookDisconnected = Runnable {
        if (IsHookConnected == null) IsHookConnected = false }

    fun RequestStatus(AppContext: Context) {
        AppContext.systemDataChannel().put(DataChannelKeys.Query_HookStatus)
        MainHandler.removeCallbacks(MarkHookDisconnected)
        MainHandler.postDelayed(MarkHookDisconnected, 500L) }

    fun MarkHookConnected(Connected: Boolean) {
        MainHandler.removeCallbacks(MarkHookDisconnected)
        IsHookConnected = Connected }

    fun RefreshFrameworkInfo(AppContext: Context) {
        IsModuleActive = YukiHook.Status.isModuleActive
        FrameworkName = YukiHook.Status.Executor.name
        FrameworkVersion = "${YukiHook.Status.Executor.version} (${YukiHook.Status.Executor.versionCode})"
        ApiVersion = YukiHook.Status.Executor.apiVersion.toString()
        RuntimeName = YukiHook.Status.runtime.explicitName
        XposedScope = YukiHook.Module.scope
        IsChecking = false
        RequestStatus(AppContext) } }

@Composable
fun MainUI() {
    var SelectedTab by rememberSaveable { mutableStateOf(MainTabs.Home) }
    val Status = remember { ModuleStatusState() }
    val AppContext = LocalContext.current.applicationContext

    DisposableEffect(Unit) {
        val MainHandler = Handler(Looper.getMainLooper())
        var IsDecided = false
        val MarkInactive = Runnable {
            if (IsDecided == false) {
                IsDecided = true
                Status.IsModuleActive = false
                Status.IsChecking = false } }

        val Subscription = YukiHook.Status.observeFrameworkService { Available ->
            MainHandler.removeCallbacks(MarkInactive)
            when {
                Available -> {
                    IsDecided = true
                    Status.RefreshFrameworkInfo(AppContext) }
                IsDecided -> {
                    Status.IsModuleActive = false
                    Status.IsChecking = false }
                else -> MainHandler.postDelayed(MarkInactive, 500L) } }

        onDispose {
            MainHandler.removeCallbacks(MarkInactive)
            Subscription?.unregister() } }

    DisposableEffect(AppContext) {
        AppContext.systemDataChannel().with {
            wait(DataChannelKeys.Response_HookSuccess) { Status.MarkHookConnected(it) }
            wait(DataChannelKeys.Response_HookEnabled) { Status.IsEnabled = it }
            wait(DataChannelKeys.Response_StrongAuthAlarmScheduleSuccess) { Succeeded ->
                Toast.makeText(AppContext,
                    if (Succeeded) R.string.Home_ModuleSettings_RequestStrongAuth_Success
                    else R.string.Home_ModuleSettings_RequestStrongAuth_Failed,
                    Toast.LENGTH_SHORT).show() } }
        onDispose { } }

    LaunchedEffect(Unit) {
        Status.RequestStatus(AppContext) }

    FreeUnlockTheme {
        Scaffold(
            bottomBar = { BottomNavigationBar(
                Status = Status,
                SelectedTab = SelectedTab,
                onTabChange = { SelectedTab = it } ) },
            modifier = Modifier.fillMaxSize() ) { InnerPadding ->

            MainContent(
                Status = Status,
                SelectedTab = SelectedTab,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(InnerPadding) ) } } }

@Composable
fun MainContent(
    Status: ModuleStatusState,
    SelectedTab: MainTabs,
    modifier: Modifier = Modifier){

    when(SelectedTab){
        MainTabs.Home -> HomeScreen(Status = Status, modifier = modifier)
        MainTabs.LogHistory -> LogHistoryScreen(modifier = modifier)
        MainTabs.About -> AboutScreen(modifier = modifier) } }

@Composable
fun HomeScreen(Status: ModuleStatusState,
               modifier: Modifier = Modifier) {
    val ActivityContext = LocalContext.current
    val AppContext = ActivityContext.applicationContext
    val PackageManager = AppContext.packageManager
    val AppInfo = AppContext.applicationInfo
    val AppName = AppInfo.loadLabel(PackageManager).toString()
    val ScopeHasSystem = Status.XposedScope
        ?.takeIf { it.isAvailable }
        ?.let { Scope -> runCatching { "system" in Scope.packages }.getOrNull() }
    val CanRequestSystemScope = (Status.IsHookConnected == false) && Status.IsModuleActive && (ScopeHasSystem == false)

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp) ) {

        Text(text = AppName,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(PaddingValues(horizontal = 8.dp, vertical = 32.dp)))

        Card(colors = when (Status.IsHookConnected) {
                true -> CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                false -> CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
                null -> CardDefaults.cardColors() },
            modifier = Modifier
                .fillMaxWidth()
                .clickable(enabled = CanRequestSystemScope) {
                    val Scope = Status.XposedScope
                    val Submitted = Scope != null && runCatching {
                        Scope.request("system") { _, _, _ ->
                            Status.RefreshFrameworkInfo(AppContext) } }.isSuccess
                    if (Submitted == false) Status.RefreshFrameworkInfo(AppContext) } ) {

            val (StatusIcon, StatusTint) = when (Status.IsHookConnected) {
                true -> Icons.Check_Fill to MaterialTheme.colorScheme.primary
                false -> Icons.Error_Fill to MaterialTheme.colorScheme.error
                null -> Icons.QuestionMark to MaterialTheme.colorScheme.onSurfaceVariant }
            val (StatusTitle, StatusDescription) = when {
                Status.IsHookConnected == null -> R.string.Home_ModuleStatus_Checking to null
                Status.IsHookConnected == true -> R.string.Home_ModuleStatus_Active to R.string.Home_ModuleStatus_ActiveDescription
                CanRequestSystemScope -> R.string.Home_ModuleStatus_Inactive to R.string.Home_ModuleStatus_InactiveDescription_TapToActivate
                Status.IsModuleActive -> R.string.Home_ModuleStatus_Inactive to R.string.Home_ModuleStatus_InactiveDescription_NeedReboot
                else -> R.string.Home_ModuleStatus_Inactive to R.string.Home_ModuleStatus_InactiveDescription_NoXposedFramework }

            Row(verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(16.dp)) {

                Icon(imageVector = StatusIcon, contentDescription = null, tint = StatusTint )
                Column {
                    Text(text = stringResource(StatusTitle),
                        color = StatusTint,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(start = 8.dp) )
                    if (StatusDescription != null) {
                        Text(text = stringResource(StatusDescription),
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(start = 8.dp) ) } } } }

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = stringResource(R.string.Home_XposedFrameworkInfo),
                    style = MaterialTheme.typography.titleMedium )
                Spacer(modifier = Modifier.height(8.dp))

                StatusRow(
                    Label = stringResource(R.string.Home_XposedFrameworkInfo_FrameworkStatus),
                    Value = when {
                        Status.IsChecking -> stringResource(R.string.Home_XposedFrameworkInfo_FrameworkStatus_Checking)
                        Status.IsModuleActive -> stringResource(R.string.Home_XposedFrameworkInfo_FrameworkStatus_Connected)
                        else -> stringResource(R.string.Home_XposedFrameworkInfo_FrameworkStatus_Disconnected) },
                    ValueColor = if (Status.IsModuleActive) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.error )
                StatusRow(
                    Label = stringResource(R.string.Home_XposedFrameworkInfo_HookStatus),
                    Value = when (Status.IsHookConnected) {
                        null -> stringResource(R.string.Home_XposedFrameworkInfo_FrameworkStatus_Checking)
                        true -> stringResource(R.string.Home_XposedFrameworkInfo_FrameworkStatus_Connected)
                        false -> stringResource(R.string.Home_XposedFrameworkInfo_FrameworkStatus_Disconnected) },
                    ValueColor = when (Status.IsHookConnected) {
                        null -> MaterialTheme.colorScheme.onSurfaceVariant
                        true -> MaterialTheme.colorScheme.primary
                        false -> MaterialTheme.colorScheme.error } )
                StatusRow(stringResource(R.string.Home_XposedFrameworkInfo_FrameworkName), Status.FrameworkName)
                StatusRow(stringResource(R.string.Home_XposedFrameworkInfo_FrameworkVersion), Status.FrameworkVersion)
                StatusRow(stringResource(R.string.Home_XposedFrameworkInfo_XposedApiVersion), Status.ApiVersion)
                StatusRow(stringResource(R.string.Home_XposedFrameworkInfo_Runtime), Status.RuntimeName) } }

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = stringResource(R.string.Home_ModuleSettings),
                    style = MaterialTheme.typography.titleMedium )
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically ) {
                    Text(
                        text = stringResource(R.string.Home_ModuleSettings_FeatureEnable),
                        style = MaterialTheme.typography.bodyMedium )
                    Spacer(modifier = Modifier.weight(1f))
                    Switch(
                        checked = Status.IsEnabled,
                        enabled = Status.IsHookConnected == true,
                        onCheckedChange = { Enabled ->
                            Status.IsEnabled = Enabled
                            AppContext.systemDataChannel()
                                .put(DataChannelKeys.Set_HookEnabled, Enabled) } ) }
                Button(
                    enabled = Status.IsHookConnected == true,
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        AppContext.systemDataChannel()
                            .put(DataChannelKeys.Request_ForceStrongAuth) } ) {
                    Text(text = stringResource(R.string.Home_ModuleSettings_RequestStrongAuth) ) } } }

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = stringResource(R.string.Home_OtherFeatures),
                    style = MaterialTheme.typography.titleMedium )
                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        try {
                            ActivityContext.startActivity(Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS) ) }
                        catch (ExceptionInstance: Exception) {
                            Toast.makeText(AppContext,
                                R.string.Home_OtherFeatures_OpenDeveloperOptions_Failed,
                                Toast.LENGTH_SHORT).show() } } ) {
                    Text(text = stringResource(R.string.Home_OtherFeatures_OpenDeveloperOptions) ) }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            val DeveloperSettingsActivity = PackageManager
                                .resolveActivity(Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS), 0)
                                ?.activityInfo
                            if (DeveloperSettingsActivity == null) {
                                Toast.makeText(AppContext,
                                    R.string.Home_OtherFeatures_OpenWirelessDebugging_Failed,
                                    Toast.LENGTH_SHORT).show() }
                            else {
                                try {
                                    ActivityContext.startActivity(Intent(TileService.ACTION_QS_TILE_PREFERENCES).apply {
                                        component = ComponentName(DeveloperSettingsActivity.packageName, DeveloperSettingsActivity.name)
                                        putExtra(Intent.EXTRA_COMPONENT_NAME,
                                            ComponentName(DeveloperSettingsActivity.packageName,
                                                $$"com.android.settings.development.qstile.DevelopmentTiles$WirelessDebugging"
                                            ) ) } ) }
                                catch (ExceptionInstance: Exception) {
                                    Toast.makeText(AppContext,
                                        R.string.Home_OtherFeatures_OpenWirelessDebugging_Failed,
                                        Toast.LENGTH_SHORT).show() } } } ) {
                        Text(text = stringResource(R.string.Home_OtherFeatures_OpenWirelessDebugging) ) } } } } } }

@Composable
fun LogHistoryScreen(modifier: Modifier = Modifier) {
    val ActivityContext = LocalContext.current
    val AppContext = ActivityContext.applicationContext
    var Logs by remember { mutableStateOf(emptyList<YLogData>()) }

    val ShareLogsTitle = stringResource(R.string.TopBar_ShareLogs)

    fun RefreshLogs() {
        AppContext.systemDataChannel().obtainLoggerInMemoryData { Data ->
            Logs = Data } }

    fun BuildLogText(): String {
        return Logs.joinToString(separator = "\n") { LogData ->
            buildString {
                append("${LogData.time} [${LogData.priority}] ${LogData.tag}: ${LogData.msg}")
                LogData.throwable?.let {
                    append('\n')
                    append(it.stackTraceToString()) } } } }

    fun BuildLogFileName(): String {
        val TimeStamp = SimpleDateFormat("yyyyMMdd-HHmmss", Locale.US).format(Date())
        return "FreeUnlock-$TimeStamp.txt" }

    fun ClearLogs() {
        AppContext.systemDataChannel().put(DataChannelKeys.Request_ClearLogs)
        Logs = emptyList() }

    fun ShareLogs() {
        runCatching {
            val LogsDirectory = File(AppContext.cacheDir, "logs").apply { mkdirs() }
            val LogsFile = File(LogsDirectory, BuildLogFileName())
            LogsFile.writeText(BuildLogText())
            val LogsUri = FileProvider.getUriForFile(
                AppContext,
                "${AppContext.packageName}.fileprovider",
                LogsFile )
            val ShareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_STREAM, LogsUri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) }
            ActivityContext.startActivity(Intent.createChooser(
                ShareIntent,
                ShareLogsTitle ) ) }
            .onFailure {
                Toast.makeText(AppContext,
                    R.string.LogHistory_ShareFailed,
                    Toast.LENGTH_SHORT).show() } }

    val ExportLogLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.CreateDocument("text/plain") ) { TargetUri ->
        if (TargetUri != null) {
            runCatching {
                val OutputStream = ActivityContext.contentResolver.openOutputStream(TargetUri)
                    ?: error("无法打开目标文件")
                OutputStream.bufferedWriter().use { Writer ->
                    Writer.write(BuildLogText()) } }
                .onFailure {
                    Toast.makeText(ActivityContext,
                        R.string.LogHistory_ExportFailed,
                        Toast.LENGTH_SHORT).show() } } }

    LaunchedEffect(Unit) {
        RefreshLogs() }

    Column(modifier = modifier) {
        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp) ) {
            IconButton(onClick = ::RefreshLogs) {
                Icon(
                    imageVector = Icons.Refresh,
                    contentDescription = stringResource(R.string.TopBar_RefreshLogs) ) }
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = ::ClearLogs) {
                Icon(
                    imageVector = Icons.Delete,
                    contentDescription = stringResource(R.string.TopBar_ClearLogs) ) }
            IconButton(onClick = { ExportLogLauncher.launch(BuildLogFileName()) }) {
                Icon(
                    imageVector = Icons.FileExport,
                    contentDescription = stringResource(R.string.TopBar_ExportLogs) ) }
            IconButton(onClick = ::ShareLogs) {
                Icon(
                    imageVector = Icons.Share,
                    contentDescription = stringResource(R.string.TopBar_ShareLogs) ) } }

        if (Logs.isEmpty()) {
            Text(
                text = stringResource(R.string.LogHistory_Empty),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant )
        } else {
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(Logs) { LogData ->
                    val PriorityColors = when (LogData.priority) {
                        "D" -> MaterialTheme.colorScheme.surfaceVariant to MaterialTheme.colorScheme.onSurfaceVariant
                        "I" -> MaterialTheme.colorScheme.primaryContainer to MaterialTheme.colorScheme.onPrimaryContainer
                        "W" -> MaterialTheme.colorScheme.tertiaryContainer to MaterialTheme.colorScheme.onTertiaryContainer
                        "E" -> MaterialTheme.colorScheme.errorContainer to MaterialTheme.colorScheme.onErrorContainer
                        else -> MaterialTheme.colorScheme.secondaryContainer to MaterialTheme.colorScheme.onSecondaryContainer }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding( horizontal = 24.dp, vertical = 8.dp) ) {

                        Column(modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)) {

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = LogData.priority,
                                    color = PriorityColors.second,
                                    style = MaterialTheme.typography.labelSmall,
                                    modifier = Modifier
                                        .clip(MaterialTheme.shapes.extraSmall)
                                        .background(PriorityColors.first)
                                        .padding(horizontal = 8.dp, vertical = 4.dp) )
                                Spacer(modifier = Modifier.weight(1f))
                                Text(
                                    text = LogData.time,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant ) }

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = LogData.msg,
                                style = MaterialTheme.typography.bodyMedium )

                            LogData.throwable?.let {
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = it.stackTraceToString(),
                                    style = MaterialTheme.typography.bodySmall,
                                    fontFamily = FontFamily.Monospace,
                                    color = MaterialTheme.colorScheme.error ) } } } } } } } }

@Composable
fun AboutScreen(modifier: Modifier = Modifier){
    val ActivityContext = LocalContext.current
    val AppContext = ActivityContext.applicationContext
    val PackageManager = AppContext.packageManager
    val AppInfo = AppContext.applicationInfo
    val AppLabel = PackageManager.getApplicationLabel(AppInfo).toString()
    val AppDescription = AppInfo.loadDescription(PackageManager).toString()
    val AppMinSDK = "Android ${AppInfo.minSdkVersion.SdkVersionName} (${AppInfo.minSdkVersion})"
    val AppTargetSDK = "Android ${AppInfo.targetSdkVersion.SdkVersionName} (${AppInfo.targetSdkVersion})"
    val SystemVersion = "Android ${Build.VERSION.RELEASE} ${Build.VERSION.SDK_INT.SdkVersionName} (${Build.VERSION.SDK_INT})"
    val AppVersionName = BuildConfig.VERSION_NAME
    val AppVersionCode = BuildConfig.VERSION_CODE
    val AppIcon = remember(AppInfo) {
        AppContext
            .packageManager
            .getApplicationIcon(AppInfo)
            .toBitmap()
            .asImageBitmap() }
    val SourceCodeURL = "https://github.com/TheWhiteDog9487/FreeUnlock"
    val ReportBugURL = "https://github.com/TheWhiteDog9487/FreeUnlock/issues"

    Column( modifier = modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState()) ) {

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally ) {

                Image(
                    bitmap = AppIcon,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(PaddingValues(top = 64.dp, bottom = 16.dp))
                        .size(160.dp)
                        .clip(MaterialTheme.shapes.medium) )
                Text(
                    text = AppLabel,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(8.dp) )
                Text(
                    text = AppDescription,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(PaddingValues(start = 32.dp, end = 32.dp, bottom = 32.dp)) ) }

            Column(modifier = Modifier.fillMaxSize()) {
                LinkRow(
                    LeftIcon = { Icon(Icons.Commit, null) },
                    "${stringResource(R.string.About_Info_AppVersionName)} $AppVersionName",
                    "${stringResource(R.string.About_Info_AppVersionCode)} $AppVersionCode",
                    "${stringResource(R.string.About_Info_MinSDK)} $AppMinSDK",
                    "${stringResource(R.string.About_Info_TargetSDK)} $AppTargetSDK",
                    "${stringResource(R.string.About_Info_SystemVersion)} $SystemVersion",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 12.dp) )
                LinkRow(
                    LeftIcon = { Icon(Icons.Code, null) },
                    stringResource(R.string.About_Link_SourceCode),
                    SourceCodeURL,
                    FirstLineUseDifferentColor = true,
                    TargetLinkURL = SourceCodeURL,
                    ActivityContext = ActivityContext,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 12.dp) )
                LinkRow(
                    LeftIcon = { Icon(Icons.Bug, null) },
                    stringResource(R.string.About_Link_ReportBug),
                    ReportBugURL,
                    FirstLineUseDifferentColor = true,
                    TargetLinkURL = ReportBugURL,
                    ActivityContext = ActivityContext,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 12.dp) ) } } }

@Composable
fun BottomNavigationBar(
    SelectedTab: MainTabs,
    Status: ModuleStatusState,
    onTabChange: (MainTabs) -> Unit,
    modifier: Modifier = Modifier ){

    NavigationBar(modifier = modifier) {

        for (Tab in MainTabs.entries) {
            NavigationBarItem(
                enabled = if (Tab != MainTabs.LogHistory) true else (Status.IsHookConnected == true),
                icon = {
                    Icon(
                        imageVector = Tab.Icon,
                        contentDescription = stringResource(Tab.Label)) },
                label = { Text(text = stringResource(Tab.Label)) },
                onClick = { onTabChange(Tab) },
                selected = SelectedTab == Tab) } } }

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    device = Devices.PHONE,
    showSystemUi = true,
    showBackground = true,
    locale = "zh")
@Composable
fun HomePhoneDarkPreview() {
    MainUI() }

@Preview(
    device = Devices.PHONE,
    showSystemUi = true,
    showBackground = true,
    locale = "zh")
@Composable
fun HomePhoneLightPreview() {
    MainUI() }