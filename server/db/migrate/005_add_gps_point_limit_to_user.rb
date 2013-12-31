class AddGpsPointLimitToUser < ActiveRecord::Migration
  def self.up
    change_table :users do |t|
      t.integer :gps_point_limit, default: 100, null: false
    end
  end

  def self.down
    change_table :users do |t|
      t.remove :gps_point_limit
    end
  end
end
