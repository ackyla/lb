class InitSchema < ActiveRecord::Migration
  def self.up
    create_table :users do |t|
      t.string :token
      t.string :name, null: false
      t.integer :gps_point, default: 0, null: false
      t.integer :level, default: 1, null: false
      t.integer :exp, default: 0, null: false
      t.timestamps
    end

    create_table :locations do |t|
      t.float :latitude
      t.float :longitude
      t.references :user
      t.timestamps
    end

    create_table :territories do |t|
      t.float :latitude, default: 0.0
      t.float :longitude, default: 0.0
      t.float :radius, default: 0.0
      t.references :owner
      t.references :character, null: false
      t.datetime "expired_time"
      t.timestamps
    end

    create_table :detections do |t|
      t.references :location
      t.references :territory
      t.timestamps
    end

    create_table :invasions do |t|
      t.references :user
      t.references :territory
      t.timestamps
    end

    add_index :users, [:id, :token], :name => "index_users_on_id_and_token", :unique => true
    add_index :detections, [:location_id, :territory_id], :name => "index_detections_on_location_and_territory", :unique => true
    add_index :invasions, [:user_id, :territory_id], :name => "index_detections_on_user_and_territory", :unique => true
  end

  def self.down
    drop_table :users
    drop_table :locations
    drop_table :detections
    drop_table :territories
    drop_table :invasions
  end
end
