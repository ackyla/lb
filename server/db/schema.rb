# encoding: UTF-8
# This file is auto-generated from the current state of the database. Instead
# of editing this file, please use the migrations feature of Active Record to
# incrementally modify your database, and then regenerate this schema definition.
#
# Note that this schema.rb definition is the authoritative source for your
# database schema. If you need to create the application database on another
# system, you should be using db:schema:load, not running all the migrations
# from scratch. The latter is a flawed and unsustainable approach (the more migrations
# you'll amass, the slower it'll run and the greater likelihood for issues).
#
# It's strongly recommended to check this file into your version control system.

ActiveRecord::Schema.define(:version => 4) do

  create_table "accounts", :force => true do |t|
    t.string   "name"
    t.string   "surname"
    t.string   "email"
    t.string   "crypted_password"
    t.string   "role"
    t.datetime "created_at",       :null => false
    t.datetime "updated_at",       :null => false
  end

  create_table "characters", :force => true do |t|
    t.string   "name",                        :null => false
    t.float    "radius",     :default => 0.0
    t.float    "precision"
    t.datetime "created_at",                  :null => false
    t.datetime "updated_at",                  :null => false
  end

  create_table "detections", :force => true do |t|
    t.integer  "location_id"
    t.integer  "territory_id"
    t.datetime "created_at",   :null => false
    t.datetime "updated_at",   :null => false
  end

  add_index "detections", ["location_id", "territory_id"], :name => "index_detections_on_location_and_territory", :unique => true

  create_table "invasions", :force => true do |t|
    t.integer  "user_id"
    t.integer  "territory_id"
    t.datetime "created_at",   :null => false
    t.datetime "updated_at",   :null => false
  end

  add_index "invasions", ["user_id", "territory_id"], :name => "index_detections_on_user_and_territory", :unique => true

  create_table "locations", :force => true do |t|
    t.float    "latitude"
    t.float    "longitude"
    t.integer  "user_id"
    t.datetime "created_at", :null => false
    t.datetime "updated_at", :null => false
  end

  create_table "notifications", :force => true do |t|
    t.integer  "user_id"
    t.integer  "detection_id"
    t.string   "notification_type"
    t.boolean  "delivered",         :default => false
    t.boolean  "read",              :default => false
    t.datetime "created_at",                           :null => false
    t.datetime "updated_at",                           :null => false
  end

  create_table "territories", :force => true do |t|
    t.float    "latitude",     :default => 0.0
    t.float    "longitude",    :default => 0.0
    t.float    "radius",       :default => 0.0
    t.integer  "owner_id"
    t.integer  "character_id",                  :null => false
    t.datetime "expired_time"
    t.datetime "created_at",                    :null => false
    t.datetime "updated_at",                    :null => false
  end

  create_table "users", :force => true do |t|
    t.string   "token"
    t.string   "name",                      :null => false
    t.integer  "gps_point",  :default => 0, :null => false
    t.integer  "level",      :default => 1, :null => false
    t.integer  "exp",        :default => 0, :null => false
    t.datetime "created_at",                :null => false
    t.datetime "updated_at",                :null => false
  end

  add_index "users", ["id", "token"], :name => "index_users_on_id_and_token", :unique => true

end
