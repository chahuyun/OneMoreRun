# OneMoreRun 插件说明

面向 Mirai Console 的回合制副本小游戏插件，支持组队、选择副本、战斗与掉落。此文档帮助快速了解项目结构与本地运行方式，详细开发指南见 `docs/` 目录。

## 功能概要
- 群内创建队伍（3 人、职业类型不可重复），选择副本难度后自动开战。
- 回合制战斗：玩家/ Boss 按顺序行动，支持技能、效果（buff/debuff）、装备属性。
- 副本掉落：权重随机或必掉；支持装备、金币、经验等类型。

## 目录结构
- `src/main/kotlin/cn/chahuyun/omr/` 核心代码
  - `OneMoreRun.kt` 插件入口
  - `plugin/` 数据加载、数据库初始化
  - `auth/` 权限注册
  - `event/` 群指令与流程控制
  - `game/` 战斗流程、实体、掉落
  - `dungeon/` 副本定义与注册
  - `equipment/` 装备系统
  - `skills/` 技能系统
  - `effect/` 效果系统
  - `entity/data/` 持久化实体（玩家、装备/技能栏）
- `docs/` 文档
  - `开发文档.md`：整体架构与流程
  - `副本/装备/技能/效果开发指南.md`：面向新手的扩展说明

## 环境要求
- JDK 17+
- Gradle（项目已附带 Wrapper：`gradlew`/`gradlew.bat`）
- 数据库：当前强制使用 MySQL（在 `DataConfig` 中配置）
- Mirai Console（用于实际运行插件）

## 本地构建
Windows：
```
gradlew.bat clean build
```
macOS / Linux：
```
./gradlew clean build
```
成功后产物位于 `build/` 目录。

## 运行配置（数据库）
编辑生成的 `config/data-config.yml`（或默认路径）：
- `type`：设置为 `MYSQL`
- `url`：如 `localhost:3306/one_more_run`
- `user` / `password`：数据库账号
当前如未配置为 MySQL，`DataManager` 会关闭 Mirai 进程。

## 快速体验流程
1) 启动 Mirai Console，加载插件 JAR。
2) 在群内发送 “创建队伍/开队/来一把” 创建队伍。
3) 其他两人发送 “加入/进组/来/join” 加入（职业类型不得重复）。
4) 队长按提示发送 “副本名称 副本等级”（如 “新手村 1”），战斗开始。

## 扩展入口
- 副本：新增 `Dungeon` 子类并注册到 `DungeonFactory`（参考 `StartingZone`）。详见 [`副本开发指南`](./docs/装备开发指南.md)。
- 装备：继承 `Equipment`，使用序列化注解并在 Registrar 中注册。详见 [`装备开发指南`](./docs/装备开发指南.md)。
- 技能：继承 `Skills`，实现效果/伤害配置后注册到 `SkillsFactory`。详见 [`技能开发指南`](./docs/效果开发指南.md)。
- 效果：继承 `Effect`，设定触发时机并注册到 `EffectFactory`。详见 [`效果开发指南`](./docs/效果开发指南.md)。

## 版权与贡献
作者：moyuyanli & firefairy198  
欢迎提交 Issue/PR 以改进功能与文档。

