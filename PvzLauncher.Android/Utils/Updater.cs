using Newtonsoft.Json;
using PvzLauncher.Android.Classes;
using PvzLauncher.Android.JsonConfigs;
using System;
using System.Collections.Generic;
using System.Text;

namespace PvzLauncher.Android.Utils
{
    public static class Updater
    {
        /// <summary>
        /// 获取远端更新信息
        /// </summary>
        /// <returns></returns>
        public static async Task<UpdateInfo.Index> GetUpdateIndex()
        {
            using (var client = new HttpClient())
                return JsonConvert.DeserializeObject<UpdateInfo.Index>(await client.GetStringAsync(AppGlobals.UpdateIndexUrl))!;
        }




    }
}
