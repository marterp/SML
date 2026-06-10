package com.mrp.sml.data.repository

import com.mrp.sml.data.local.db.dao.DeviceDao
import com.mrp.sml.data.local.db.entities.DeviceEntity
import com.mrp.sml.data.mapper.DeviceMapper
import com.mrp.sml.data.remote.discovery.DeviceDiscoveryManager
import com.mrp.sml.domain.model.DeviceModel
import com.mrp.sml.domain.repository.DeviceRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeviceRepositoryImpl @Inject constructor(
    private val deviceDiscoveryManager: DeviceDiscoveryManager,
    private val deviceDao: DeviceDao
) : DeviceRepository {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private var connectedDevice: DeviceModel? = null

    override fun observeDiscoveredDevices(): Flow<List<DeviceModel>> {
        return deviceDiscoveryManager.discoveredDevices.map { devices ->
            devices.map { DeviceMapper.coreToDomain(it) }
        }
    }

    override suspend fun connectToDevice(deviceId: String) {
        deviceDiscoveryManager.connectToDevice(deviceId)
    }

    override suspend fun disconnect() {
        deviceDiscoveryManager.stopDiscovery()
        connectedDevice = null
    }

    override suspend fun getConnectedDevice(): DeviceModel? {
        return connectedDevice
    }

    override fun isConnected(): Boolean {
        return connectedDevice != null
    }

    fun onDeviceConnected(deviceModel: DeviceModel) {
        connectedDevice = deviceModel
        scope.launch {
            val entity = DeviceEntity(
                deviceId = deviceModel.id,
                deviceName = deviceModel.name,
                ipAddress = deviceModel.ipAddress
            )
            deviceDao.insert(entity)
        }
    }
}
