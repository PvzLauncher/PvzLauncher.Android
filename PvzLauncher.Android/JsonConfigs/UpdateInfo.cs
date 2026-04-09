using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Text;

namespace PvzLauncher.Android.JsonConfigs
{
    public class UpdateInfo
    {
        public class Index
        {
            [JsonProperty("version")]
            public string Version { get; set; }

            [JsonProperty("url")]
            public string Url { get; set; }

            [JsonProperty("log")]
            public string[] Log { get; set; }
        }
    }
}
