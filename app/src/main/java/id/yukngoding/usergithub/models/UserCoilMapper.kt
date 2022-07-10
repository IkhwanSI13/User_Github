package id.yukngoding.usergithub.models

import coil.map.Mapper

class UserCoilMapper : Mapper<Users, String> {
    override fun map(data: Users) = data.avatar_url
}